package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.OverviewController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Overview implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;
  private final OverviewController controller = new OverviewController(this);
  private boolean hasStarted = false;
  private boolean isPause = false;
  @Getter @Setter private double speedMultiplier;
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
    setSpeedMultiplier(value);
    System.out.println("Slider speed changed to x" + value);
  }

  @FXML
  public void stopSimulation(ActionEvent actionEvent) {
    controller.stopEngine();
    resetSimulation();
    controller.logRun();
    main.showDatabaseViewer();
    controller.resetMetricsCollectors();
    hasStarted = false;
  }

  private void resetSimulation() {
    sliderSpeed.setValue(0);
    buttonStatus.setText("Start");
  }

  // Animation controls

  @FXML private ProgressBar barToAssembly;
  @FXML private ProgressBar barToErrorCheck;
  @FXML private ProgressBar barToFix;
  @FXML private ProgressBar barToFixUp1;
  @FXML private ProgressBar barToFixUp2;
  @FXML private ProgressBar barToFixDown1;
  @FXML private ProgressBar barToFixDown2;
  @FXML private ProgressBar barToFixDown3;
  @FXML private ProgressBar barToFixFitting;
  @FXML private ProgressBar barToFixBolting;
  @FXML private ProgressBar barToFixRiveting;
  @FXML private ProgressBar barToFixWelding;
  @FXML private ProgressBar barToFixPosition;
  @FXML private ProgressBar barFromFitting;
  @FXML private ProgressBar barFromBolting;
  @FXML private ProgressBar barFromRiveting;
  @FXML private ProgressBar barFromWelding;
  @FXML private ProgressBar barFromPosition;
  @FXML private ProgressBar barDown1;
  @FXML private ProgressBar barDown2;
  @FXML private ProgressBar barDown3;
  @FXML private ProgressBar barDown4;
  @FXML private ProgressBar barDown5;
  @FXML private ProgressBar barDeparture1;
  @FXML private ProgressBar barDeparture2;

  private long calculateAnimationSpeed() {
    long animationSpeed;
    if (speedMultiplier < 0) {
      // If slowing down
      animationSpeed = (Math.round(speedMultiplier / -1) * 100);
    } else if (speedMultiplier > 0) {
      // If speeding up
      animationSpeed = 100 / Math.round(speedMultiplier);
    } else {
      // If staying at default
      animationSpeed = 100;
    }
    return animationSpeed;
  }

  private long calculateWaitingTime() {
    long waitingTime;
    if (speedMultiplier < 0) {
      // If slowing down
      waitingTime = (Math.round(speedMultiplier / -1) * 200);
    } else if (speedMultiplier > 0) {
      // If speeding up
      waitingTime = 200 / Math.round(speedMultiplier);
    } else {
      // If staying at default
      waitingTime = 200;
    }
    return waitingTime;
  }

  @SneakyThrows
  private void animateProgressBar(ProgressBar progressBar) {
    val animationSpeed = calculateAnimationSpeed();
    val waitingTime = calculateWaitingTime();

    var currentProgress = 0.1;

    for (int i = 0; i < 10; i++) {
      progressBar.setProgress(currentProgress);
      currentProgress += 0.1;
      Thread.sleep(animationSpeed);
    }

    Thread.sleep(waitingTime);

    progressBar.setProgress(0);
  }

  @SneakyThrows
  private void animateProgressBarsSequential(ProgressBar[] progressBars) {
    val animationSpeed = calculateAnimationSpeed();
    val waitingTime = calculateWaitingTime();
    var currentProgress = 0.1;

    for (int i = 0; i < progressBars.length; i++) {
      val previous = i > 0 ? progressBars[i - 1] : null;
      val current = progressBars[i];
      val next = i < progressBars.length - 1 ? progressBars[i + 1] : null;

      // Mid-run
      if (previous != null) {
        previous.setProgress(0);
      }

      runAnimationCycle(current, currentProgress, animationSpeed, waitingTime);

      if (previous != null && previous.getProgress() >= 0.98) { // Mid-run
        previous.setProgress(0);
        currentProgress = 0.1;
      } else if (next == null) { // End of run
        current.setProgress(0);
      }
    }
  }

  @SneakyThrows
  private void runAnimationCycle(
      ProgressBar progressBar, double currentProgress, long animationSpeed, long waitingTime) {

    for (int i = 0; i < 10; i++) {
      progressBar.setProgress(currentProgress);
      currentProgress += 0.1;
      Thread.sleep(animationSpeed);
    }

    Thread.sleep(waitingTime);
  }

  public void materialToAssembly() {
    animateProgressBar(barToAssembly);
  }

  public void materialToErrorCheck() {
    animateProgressBar(barToErrorCheck);
  }

  public void materialToFitting() {
    ProgressBar[] progressBars = {barToFix, barToFixUp2, barToFixUp1, barToFixFitting};
    animateProgressBarsSequential(progressBars);
  }

  public void materialToBolting() {
    ProgressBar[] progressBars = {barToFix, barToFixUp2, barToFixBolting};
    animateProgressBarsSequential(progressBars);
  }

  public void materialToRiveting() {
    ProgressBar[] progressBars = {barToFix, barToFixRiveting};
    animateProgressBarsSequential(progressBars);
  }

  public void materialToWelding() {
    ProgressBar[] progressBars = {barToFix, barToFixDown1, barToFixWelding};
    animateProgressBarsSequential(progressBars);
  }

  public void materialToPosition() {
    ProgressBar[] progressBars = {barToFix, barToFixDown1, barToFixDown2, barToFixPosition};
    animateProgressBarsSequential(progressBars);
  }

  public void materialFromFitting() {
    ProgressBar[] progressBars = {
      barFromFitting, barDown1, barDown2, barDown3, barDown4, barDown5, barDeparture2
    };
    animateProgressBarsSequential(progressBars);
  }

  public void materialFromBolting() {
    ProgressBar[] progressBars = {
      barFromBolting, barDown2, barDown3, barDown4, barDown5, barDeparture2
    };
    animateProgressBarsSequential(progressBars);
  }

  public void materialFromRiveting() {
    ProgressBar[] progressBars = {barFromRiveting, barDown3, barDown4, barDown5, barDeparture2};
    animateProgressBarsSequential(progressBars);
  }

  public void materialFromWelding() {
    ProgressBar[] progressBars = {barFromWelding, barDown4, barDown5, barDeparture2};
    animateProgressBarsSequential(progressBars);
  }

  public void materialFromPosition() {
    ProgressBar[] progressBars = {barFromPosition, barDown5, barDeparture2};
    animateProgressBarsSequential(progressBars);
  }

  public void materialToDeparture() {
    ProgressBar[] progressBars = {
      barToFix, barToFixDown1, barToFixDown2, barToFixDown3, barDeparture1, barDeparture2
    };
    animateProgressBarsSequential(progressBars);
  }
}
