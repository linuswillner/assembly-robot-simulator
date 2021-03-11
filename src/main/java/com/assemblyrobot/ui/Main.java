package com.assemblyrobot.ui;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.Configuration;
import com.assemblyrobot.shared.utils.EnvUtils;
import com.assemblyrobot.ui.controllers.StationViewerController;
import com.assemblyrobot.ui.views.View;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
  @Getter private Stage primaryStage;

  @Getter @Setter private StationViewerController stationViewerController;

  private Parent rootScene;
  private final Configuration config = Config.getConfig();
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void start(Stage primaryStage) {
    EnvUtils.checkEnv();
    this.primaryStage = primaryStage;

    setScene(
        "/scenes/StationViewer.fxml", "Assembly Robot Simulator - Station Viewer", false, false);
    setScene("/scenes/Overview.fxml", "Assembly Robot Simulator", false, true);

    primaryStage.setOnCloseRequest(
        event -> {
          Platform.exit();
          System.exit(0); // Also close dangling simulator threads
        });
  }

  /**
   * Internal callback for initialising a stage based on application configuration.
   *
   * @param stage The {@link Stage} to configure.
   * @param stageTitle The title of the {@link Stage}.
   * @param isPopup Whether this stage is a popup (affects window sizing).
   */
  private void initStage(Stage stage, String stageTitle, boolean isPopup) {
    // Set stage title
    stage.setTitle(stageTitle);

    val appSettings = config.getAppSettings();

    val windowHeight = isPopup ? appSettings.getPopupWindowHeight() : appSettings.getWindowHeight();
    val windowWidth = isPopup ? appSettings.getPopupWindowWidth() : appSettings.getWindowWidth();

    // Lock window size
    stage.setMinWidth(windowWidth);
    stage.setMaxWidth(windowWidth);
    stage.setMinHeight(windowHeight);
    stage.setMaxHeight(windowHeight);
  }

  /**
   * Initialises a {@link Scene} with application-specific options through a call to {@link
   * Main#initStage(Stage, String, boolean)}.
   *
   * @param sceneResourcePath The resource path for the FXML file of this {@link Scene}.
   * @param stageTitle The title of the stage.
   * @param isPopup Whether this scene is supposed to be a popup (Changes window sizing).
   * @param isPrimary Whether this is the primary scene.
   */
  private void setScene(
      String sceneResourcePath, String stageTitle, boolean isPopup, boolean isPrimary) {
    try {
      Stage stage;

      if (isPrimary) {
        stage = primaryStage;
      } else {
        stage = new Stage();
      }

      initStage(stage, stageTitle, isPopup);

      val loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(sceneResourcePath));
      rootScene = loader.load();

      View controller = loader.getController();
      controller.setMain(this);
      controller.setStage(stage);

      val scene = new Scene(rootScene);
      stage.setScene(scene);
      stage.show();
      controller.afterInitialize();

      logger.trace("Opening view {}.", sceneResourcePath);
    } catch (IOException e) {
      logger.error("Could not open view {}:", sceneResourcePath, e);
    }
  }

  /** Shows the {@link com.assemblyrobot.ui.views.DatabaseViewer}. */
  public void showDatabaseViewer() {
    setScene(
        "/scenes/DatabaseViewer.fxml", "Assembly Robot Simulator - Database Viewer", false, false);
  }

  /** Shows the {@link com.assemblyrobot.ui.views.OptionsEditor}. */
  public void showOptionsEditor() {
    setScene(
        "/scenes/OptionsEditor.fxml", "Assembly Robot Simulator - Options Editor", false, false);
  }

  /** Shows the {@link com.assemblyrobot.ui.views.About} window. */
  public void showAbout() {
    setScene("/scenes/About.fxml", "Assembly Robot Simulator - About", true, false);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
