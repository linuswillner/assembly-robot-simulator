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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Overview implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;
  private final OverviewController controller = new OverviewController();
  private boolean hasStarted = false;
  private boolean isPause = false;
  private static final Logger logger = LogManager.getLogger();

  @FXML private Button buttonStatus;
  @FXML private Slider sliderSpeed;

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  public void openOptionsEditor(ActionEvent actionEvent) {
    main.showOptionsEditor();
  }

  @FXML
  public void openDatabaseViewer(ActionEvent actionEvent) {
    main.showDatabaseViewer();
  }

  @FXML
  public void openAbout(ActionEvent actionEvent) {
    main.showAbout();
  }

  @FXML
  public void controlSimulation(ActionEvent actionEvent) {
    if (!hasStarted) {
      main.showStationViewer();
      buttonStatus.setText("Pause");
      controller.startEngine();
      hasStarted = true;
    } else {
      if (!isPause) {
        buttonStatus.setText("Resume");
        isPause = true;
        controller.setPause(true);
        controller.setCanProceed(false);
      } else {
        buttonStatus.setText("Pause");
        isPause = false;
        controller.setPause(false);
        controller.setCanProceed(true);
        logger.trace("Simulation resumed.");
      }
    }
  }

  @FXML
  public void takeStep(ActionEvent actionEvent) {
    controller.takeStep();
  }

  @FXML
  public void onSliderChanged(MouseEvent mouseEvent) {
    double value = sliderSpeed.getValue();
    controller.setSpeed(value);
    System.out.println("Slider speed changed to x" + value);
  }

  @FXML
  public void stopSimulation(ActionEvent actionEvent) {
    controller.stopEngine();
    resetSimulation();
    controller.logRun();
    main.showDatabaseViewer();
    controller.resetMetricsCollectors();
  }

  private void resetSimulation() {
    sliderSpeed.setValue(0);
    buttonStatus.setText("Start");
  }
}
