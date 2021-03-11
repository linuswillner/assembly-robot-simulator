package com.assemblyrobot.ui;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.shared.utils.EnvUtils;
import com.assemblyrobot.ui.controllers.StationViewerController;
import com.assemblyrobot.ui.views.View;
import java.io.IOException;
import javafx.application.Application;
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
  private final ApplicationConfig config = Config.getConfig();
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void start(Stage primaryStage) {
    EnvUtils.checkEnv();

    this.primaryStage = primaryStage;
    initStage(primaryStage, "Assembly Robot Simulator", false);
    setScene("/scenes/StationViewer.fxml", "Assembly Robot Simulator - Station Viewer", false);
    setScene("/scenes/Overview.fxml", "Assembly Robot Simulator", false);
  }

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

  private void setScene(String sceneResourcePath, String stageTitle, boolean isPopup) {
    try {
      val stage = new Stage();
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

  public void showStationViewer() {
    setScene("/scenes/StationViewer.fxml", "Assembly Robot Simulator - Station Viewer", false);
  }

  public void showDatabaseViewer() {
    setScene("/scenes/DatabaseViewer.fxml", "Assembly Robot Simulator - Database Viewer", false);
  }

  public void showOptionsEditor() {
    setScene("/scenes/OptionsEditor.fxml", "Assembly Robot Simulator - Options Editor", false);
  }

  public void showAbout() {
    setScene("/scenes/About.fxml", "Assembly Robot Simulator - About", true);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
