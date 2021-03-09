package com.assemblyrobot.ui;

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
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Assembly Robot Simulator");
    val width = 1024;
    val height = 740;
    primaryStage.setMinWidth(width);
    primaryStage.setMaxWidth(width);
    primaryStage.setMinHeight(height);
    primaryStage.setMaxHeight(height);

    initOverviewLayout();
  }

  public void initOverviewLayout() {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/scenes/Overview.fxml"));
      rootScene = loader.load();

      Overview controller = loader.getController();
      controller.setMain(this);

      Scene scene = new Scene(rootScene);

      primaryStage.setScene(scene);
      primaryStage.show();

      logger.trace("Opening Main view.");

    } catch (IOException e) {
      logger.error("Opening Main view failed. Resource not found", e);
    }
  }

  public void showStationViewer() {
    try {
      Stage stage = new Stage();
      val width = 1024;
      val height = 740;
      stage.setMinWidth(width);
      stage.setMaxWidth(width);
      stage.setMinHeight(height);
      stage.setMaxHeight(height);
      stage.setTitle("Assembly Robot Simulator - Station Viewer");

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/scenes/StationViewer.fxml"));
      rootScene = loader.load();

      Scene scene = new Scene(rootScene);
      stage.setScene(scene);
      stage.show();

      logger.trace("Opening StationViewer view.");

    } catch (IOException e) {
      logger.error("Opening StationViewer view failed. Resource not found", e);
    }
  }

  public void showDatabaseViewer() {
    try {
      Stage stage = new Stage();
      val width = 1024;
      val height = 740;
      stage.setMinWidth(width);
      stage.setMaxWidth(width);
      stage.setMinHeight(height);
      stage.setMaxHeight(height);
      stage.setTitle("Assembly Robot Simulator - Database Viewer");

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/scenes/DatabaseViewer.fxml"));
      rootScene = loader.load();

      Scene scene = new Scene(rootScene);
      stage.setScene(scene);
      stage.show();

      logger.trace("Opening DatabaseViewer view.");

    } catch (IOException e) {
      logger.error("Opening DatabaseViewer view failed. Resource not found", e);
    }
  }

  public void showOptionsEditor() {
    try {
      Stage stage = new Stage();
      val width = 1024;
      val height = 740;
      stage.setMinWidth(width);
      stage.setMaxWidth(width);
      stage.setMinHeight(height);
      stage.setMaxHeight(height);
      stage.setTitle("Assembly Robot Simulator - Options Editor");

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/scenes/OptionsEditor.fxml"));
      rootScene = loader.load();

      Scene scene = new Scene(rootScene);
      stage.setScene(scene);
      stage.show();

      logger.trace("Opening OptionsEditor view.");

    } catch (IOException e) {
      logger.error("Opening OptionsEditor view failed. Resource not found", e);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
