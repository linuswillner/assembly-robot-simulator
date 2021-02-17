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

public abstract class Config {
  private static final Logger logger = LogManager.getLogger();
  private static ApplicationConfig config;

  public static ApplicationConfig getConfig() {
    if (config == null) {
      load();
    }

    return config;
  }

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
