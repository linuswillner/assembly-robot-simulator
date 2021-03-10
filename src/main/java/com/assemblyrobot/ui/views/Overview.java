package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.OverviewController;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import lombok.Setter;

public class Overview implements Initializable, View {

  @Setter private Main main;
  private final OverviewController controller = new OverviewController();
  private boolean hasStarted = false;
  private boolean isPause = false;

  @FXML private Button buttonStatus;
  @FXML private Slider sliderSpeed;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @Override
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
