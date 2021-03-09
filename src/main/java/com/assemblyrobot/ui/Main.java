package com.assemblyrobot.ui;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ApplicationConfig;
import com.assemblyrobot.ui.views.Overview;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

  private Stage primaryStage;
  private Parent rootScene;
  private final ApplicationConfig config = Config.getConfig();
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Assembly Robot Simulator");
    initOverviewLayout();
  }

  private void initStage(Stage stage, String stageTitle) {
    // Set stage title
    stage.setTitle(stageTitle);

    val windowHeight = config.getAppSettings().getWindowHeight();
    val windowWidth = config.getAppSettings().getWindowWidth();

    // Lock window size
    stage.setMinWidth(windowWidth);
    stage.setMaxWidth(windowWidth);
    stage.setMinHeight(windowHeight);
    stage.setMaxHeight(windowHeight);
  }

  private void setScene(String sceneResourcePath, String stageTitle) {
    try {
      val stage = new Stage();
      initStage(stage, stageTitle);

      val loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(sceneResourcePath));
      rootScene = loader.load();

      val scene = new Scene(rootScene);
      stage.setScene(scene);
      stage.show();

      logger.trace("Opening view {}.", sceneResourcePath);
    } catch (IOException e) {
      logger.error("Could not open view {}:", sceneResourcePath, e);
    }
  }

  public void initOverviewLayout() {
    try {
      val loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/scenes/Overview.fxml"));
      rootScene = loader.load();

      Overview controller = loader.getController();
      controller.setMain(this);

      val scene = new Scene(rootScene);

      primaryStage.setScene(scene);
      primaryStage.show();

      logger.trace("Initialising main layout.");

    } catch (IOException e) {
      logger.error("Could not initialise main layout:", e);
    }
  }

  public void showStationViewer() {
    setScene("/scenes/StationViewer.fxml", "Assembly Robot Simulator - Station Viewer");
  }

  public void showDatabaseViewer() {
    setScene("/scenes/DatabaseViewer.fxml", "Assembly Robot Simulator - Database Viewer");
  }

  public void showOptionsEditor() {
    setScene("/scenes/OptionsEditor.fxml", "Assembly Robot Simulator - Options Editor");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
