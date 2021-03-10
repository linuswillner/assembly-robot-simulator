package com.assemblyrobot.shared.config;

import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.utils.JsonUtils;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Central configuration loader and manager class. */
public abstract class Config {
  private static final Logger logger = LogManager.getLogger();
  private static ApplicationConfig config;
  private static final Preferences userSettings =
      Preferences.userRoot().node(Config.class.getName());
  private static final String DEFAULT_CONFIG_PATH = "/config/config.json";

  @RequiredArgsConstructor
  public enum UserSetting {
    FIRST_RUN("first_run"),
    CUSTOM_CONFIG_PATH("custom_config_path"),
    CUSTOM_CONFIG_LOADED("custom_config_loaded");

    @Getter private final String preferenceName;
  }

  /**
   * Returns the configuration as defined in resources/config/config.json, or in an optional custom
   * path if specified by the user.
   *
   * @return {@link ApplicationConfig}
   */
  public static ApplicationConfig getConfig() {
    if (config == null) {
      load();
    }

    return config;
  }

  public static void updateConfig(ApplicationConfig newConfig) {
    config = newConfig;
  }

  /**
   * (Re)loads the configuration file from either the default path ({@link
   * Config#DEFAULT_CONFIG_PATH}) or a custom path as specified by the user.
   *
   * <p>An exception in the loading process of a custom config will cause an all-or-nothing fallback
   * to the default configuration.
   */
  public static void load() {
    val useCustomConfig = hasUserSetting(UserSetting.CUSTOM_CONFIG_PATH);

    if (useCustomConfig) {
      var configPath = getUserSetting(UserSetting.CUSTOM_CONFIG_PATH);

      try (val file = new FileReader(configPath)) {
        config = JsonUtils.gson.fromJson(file, ApplicationConfig.class);
        putUserSetting(UserSetting.CUSTOM_CONFIG_LOADED, true);
      } catch (Exception e) {
        logger.error("Could not load custom config:", e);
        fallbackToDefaultConfig(); // Fallback to default
      }
    } else {
      fallbackToDefaultConfig();
    }
  }

  /**
   * Internal method to initiate a all-or-nothing fallback to the default config, should the custom
   * config loading somehow fail. If this method fails to load the default config (Something that
   * should conceivably never happen due to it being included in /resources), the application will
   * exit.
   */
  private static void fallbackToDefaultConfig() {
    logger.info("Falling back to default config.");

    try {
      removeUserSetting(UserSetting.CUSTOM_CONFIG_PATH);
      removeUserSetting(UserSetting.CUSTOM_CONFIG_LOADED);

      val configPath = Config.class.getResource(DEFAULT_CONFIG_PATH);
      val configFile = Paths.get(configPath.toURI()).toFile();
      val file = new FileReader(configFile.getAbsolutePath());
      config = JsonUtils.gson.fromJson(file, ApplicationConfig.class);
      file.close();
    } catch (Exception e) {
      // If we can't load the default config, we're boned anyway
      logger.fatal("Could not load default config:", e);
      System.exit(1);
    }
  }

  /**
   * Checks if a user setting exists.
   *
   * @param setting {@link UserSetting}
   * @return {@link Boolean} indicating whether the setting exists.
   */
  public static boolean hasUserSetting(UserSetting setting) {
    return userSettings.get(setting.getPreferenceName(), null) != null;
  }

  /**
   * Gets a user setting as a string, or null if not defined.
   *
   * @param setting {@link UserSetting}
   * @return {@link String} or null if not found.
   */
  public static String getUserSetting(UserSetting setting) {
    return userSettings.get(setting.getPreferenceName(), null);
  }

  /**
   * Gets a user setting as a boolean.
   *
   * <p>IMPORTANT: Due to a combinatory poor design choice in the {@link Preferences} API and how
   * Java handles primitive types, this method is forced to use a boolean (Not null) as a fallback
   * value for undefined settings. As a result, it uses "false" as the better security choice.
   * Subsequently, it's HIGHLY RECOMMENDED to use {@link Config#hasUserSetting(UserSetting)} to
   * check for the existence of any given setting before running this method, in order to avoid
   * unexpected results.
   *
   * @param setting {@link UserSetting}
   * @return {@link Boolean}
   */
  public static boolean getUserSettingBoolean(UserSetting setting) {
    return userSettings.getBoolean(setting.getPreferenceName(), false);
  }

  /**
   * Defines a user setting as a string value.
   *
   * @param setting {@link UserSetting}
   * @param value {@link String}
   */
  public static void putUserSetting(UserSetting setting, String value) {
    userSettings.put(setting.getPreferenceName(), value);
  }

  /**
   * Defines a user setting as a boolean value.
   *
   * @param setting {@link UserSetting}
   * @param value {@link Boolean}
   */
  public static void putUserSetting(UserSetting setting, boolean value) {
    userSettings.putBoolean(setting.getPreferenceName(), value);
  }

  /**
   * Removes a user setting.
   *
   * @param setting {@link UserSetting}
   */
  public static void removeUserSetting(UserSetting setting) {
    userSettings.remove(setting.getPreferenceName());
  }
}
