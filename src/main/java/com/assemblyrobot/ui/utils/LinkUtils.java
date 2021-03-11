package com.assemblyrobot.ui.utils;

import java.awt.Desktop;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Generic link utilities. */
public abstract class LinkUtils {
  private static final Logger logger = LogManager.getLogger();

  /**
   * Opens a URL in the user's default browser on supported operating systems.
   *
   * @param url URL to open. Malformed URLs will result in logged errors, but no throws, as this is
   *     supposed to be a set-it-and-forget-it solution.
   */
  public static void openURLInBrowser(String url) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(new URI(url));
      } catch (Exception e) {
        logger.error("Could not open URL {} in browser:", url, e);
      }
    }
  }
}
