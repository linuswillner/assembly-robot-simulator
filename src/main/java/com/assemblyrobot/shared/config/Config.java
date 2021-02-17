package com.assemblyrobot.shared.config;

import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.utils.JsonUtils;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Config loader and manager class. */
public abstract class Config {
  private static final Logger logger = LogManager.getLogger();
  private static ApplicationConfig config;

  /**
   * Returns the configuration as defined in resources/config/config.json.
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
   * (Re)loads the configuration file from resources/config/config.json.
   *
   * <p>An IOException in this method will cause the program to exit with code 1, as the config must
   * be loaded in order for the application to function.
   */
  @SneakyThrows
  public static void load() {
    val configFile = Config.class.getResource("/config/config.json");
    val configPath = Paths.get(configFile.toURI()).toFile();

    try (val file = new FileReader(configPath.getAbsolutePath())) {
      config = JsonUtils.gson.fromJson(file, ApplicationConfig.class);
    } catch (IOException e) {
      logger.error("Config loading failed: ", e);

      // Config loading errors are fatal, exit
      logger.error("Exiting.");
      System.exit(1);
    }
  }
}
