package com.assemblyrobot.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent rootScene = FXMLLoader.load(getClass().getResource("/scenes/Main.fxml"));
    primaryStage.setTitle("Assembly Robot Simulator");
    primaryStage.setScene(new Scene(rootScene));
    primaryStage.show();

    val width = 1024;
    val height = 740;
    primaryStage.setMinWidth(width);
    primaryStage.setMaxWidth(width);
    primaryStage.setMinHeight(height);
    primaryStage.setMaxHeight(height);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
