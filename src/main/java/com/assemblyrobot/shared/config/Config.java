package com.assemblyrobot.shared.config;

import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.utils.JsonUtils;
import java.io.File;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.prefs.Preferences;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Config loader and manager class. */
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
      var configPath = Config.class.getResource(getUserSetting(UserSetting.CUSTOM_CONFIG_PATH));

      File configFile = null;

      try {
        configFile = Paths.get(configPath.toURI()).toFile();
      } catch (URISyntaxException e) {
        logger.error("Could not parse custom config path:", e);
        fallbackToDefaultConfig(); // Fallback to default
      }

      // Only load custom if we didn't fallback to the default config due to a URISyntaxException
      if (configFile != null) {
        try (val file = new FileReader(configFile.getAbsolutePath())) {
          config = JsonUtils.gson.fromJson(file, ApplicationConfig.class);
          putUserSetting(UserSetting.CUSTOM_CONFIG_LOADED, true);
        } catch (Exception e) {
          logger.error("Could not load custom config:", e);
          fallbackToDefaultConfig(); // Fallback to default
        }
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
    try {
      putUserSetting(UserSetting.CUSTOM_CONFIG_LOADED, false);
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

  public static boolean hasUserSetting(UserSetting setting) {
    return userSettings.get(setting.getPreferenceName(), null) != null;
  }

  public static String getUserSetting(UserSetting setting) {
    return userSettings.get(setting.getPreferenceName(), null);
  }

  public static boolean getUserSettingBoolean(UserSetting setting) {
    return userSettings.getBoolean(setting.getPreferenceName(), false);
  }

  public static void putUserSetting(UserSetting setting, String value) {
    userSettings.put(setting.getPreferenceName(), value);
  }

  public static void putUserSetting(UserSetting setting, boolean value) {
    userSettings.putBoolean(setting.getPreferenceName(), value);
  }

  public static void removeUserSetting(UserSetting setting) {
    userSettings.remove(setting.getPreferenceName());
  }
}
