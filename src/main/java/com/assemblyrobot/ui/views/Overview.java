package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.Main;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class Overview implements Initializable {

  private Main main;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void setMain(Main main) {
    this.main = main;
  }

  @FXML
  public void openOptionsEditor(ActionEvent actionEvent) {
    main.showOptionsEditor();
  }

  @FXML
  public void openDatabaseViewer(ActionEvent actionEvent) {
    main.showDatabaseViewer();
  }

  @FXML
  public void saveRun(ActionEvent actionEvent) {}

  @FXML
  public void openAbout(ActionEvent actionEvent) {}
}
