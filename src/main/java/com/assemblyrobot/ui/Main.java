package com.assemblyrobot.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent rootScene = FXMLLoader.load(getClass().getResource("/scenes/Main.fxml"));
    primaryStage.setTitle("Hello World");
    primaryStage.setScene(new Scene(rootScene));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
