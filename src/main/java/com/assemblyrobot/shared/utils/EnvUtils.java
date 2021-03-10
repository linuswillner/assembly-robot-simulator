package com.assemblyrobot.shared.utils;

import com.assemblyrobot.shared.config.Config;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EnvUtils {
  private static final Logger logger = LogManager.getLogger();

  /**
   * Checks that the mandatory environment variables (as defined by /config/config.json) are
   * present.
   *
   * <p>If all environment variables are not present, the application will exit.
   */
  public static void checkEnv() {
    val requiredKeys = Config.getConfig().getAppSettings().getRequiredEnv();
    val missingKeys = new ArrayList<String>();

    requiredKeys.forEach(
        variable -> {
          if (System.getenv(variable) == null || System.getenv().isEmpty()) {
            missingKeys.add(variable);
          }
        });

    if (missingKeys.size() > 0) {
      logger.fatal(
          "Cannot start application; misconfigured environment variables. The following environment variables were marked as required but are missing: {}",
          Arrays.toString(missingKeys.toArray()));
      System.exit(1);
    }
  }
}
