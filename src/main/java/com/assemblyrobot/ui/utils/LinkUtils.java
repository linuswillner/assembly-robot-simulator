package com.assemblyrobot.ui.utils;

import java.awt.Desktop;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LinkUtils {
  private static final Logger logger = LogManager.getLogger();

  public static void openURLInBrowser(String url) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop()
            .browse(new URI(url));
      } catch (Exception e) {
        logger.error("Could not open URL {} in browser:", url, e);
      }
    }
  }
}
