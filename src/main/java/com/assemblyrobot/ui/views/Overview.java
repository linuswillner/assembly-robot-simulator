package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.OverviewController;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
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
    // controller.logRun();
    main.showDatabaseViewer();
    controller.resetMetricsCollectors();
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
  public void materialToAssembly() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToAssembly.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    barToAssembly.setProgress(0);
  }

  @SneakyThrows
  public void materialToErrorCheck() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToErrorCheck.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    barToErrorCheck.setProgress(0);
  }

  @SneakyThrows
  public void materialToFitting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    // if (barToFix.getProgress() == 0) {
    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixUp2.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barToFixUp2.getProgress() >= 0.98) {
        barToFixUp2.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barToFixUp1.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barToFixUp1.getProgress() >= 0.98) {
          barToFixUp1.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barToFixFitting.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);
          barToFixFitting.setProgress(0);
        }
      }
    }
    // }
  }

  @SneakyThrows
  public void materialToBolting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);

      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixUp2.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);
      if (barToFixUp2.getProgress() >= 0.98) {
        barToFixUp2.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barToFixBolting.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);
        barToFixBolting.setProgress(0);
      }
    }
  }

  @SneakyThrows
  public void materialToRiveting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);

      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixRiveting.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);
      barToFixRiveting.setProgress(0);
    }
  }

  @SneakyThrows
  public void materialToWelding() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);

      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixDown1.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);
      if (barToFixDown1.getProgress() >= 0.98) {
        barToFixDown1.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barToFixWelding.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);
        barToFixWelding.setProgress(0);
      }
    }
  }

  @SneakyThrows
  public void materialToPosition() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixDown1.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barToFixDown1.getProgress() >= 0.98) {
        barToFixDown1.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barToFixDown2.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barToFixDown2.getProgress() >= 0.98) {
          barToFixDown2.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barToFixPosition.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);
          barToFixPosition.setProgress(0);
        }
      }
    }
  }

  @SneakyThrows
  public void materialFromFitting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barFromFitting.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barFromFitting.getProgress() >= 0.98) {
      barFromFitting.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barDown1.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barDown1.getProgress() >= 0.98) {
        barDown1.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barDown2.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barDown2.getProgress() >= 0.98) {
          barDown2.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barDown3.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);

          if (barDown3.getProgress() >= 0.98) {
            barDown3.setProgress(0);
            progress = 0.1;
            for (int i = 0; i < 10; i++) {
              barDown4.setProgress(progress);
              progress += 0.1;
              Thread.sleep(animationSpeed);
            }
            Thread.sleep(waitingTime);

            if (barDown4.getProgress() >= 0.98) {
              barDown4.setProgress(0);
              progress = 0.1;
              for (int i = 0; i < 10; i++) {
                barDown5.setProgress(progress);
                progress += 0.1;
                Thread.sleep(animationSpeed);
              }
              Thread.sleep(waitingTime);

              if (barDown5.getProgress() >= 0.98) {
                barDown5.setProgress(0);
                progress = 0.1;
                for (int i = 0; i < 10; i++) {
                  barDeparture2.setProgress(progress);
                  progress += 0.1;
                  Thread.sleep(animationSpeed);
                }
                Thread.sleep(waitingTime);
                barDeparture2.setProgress(0);
              }
            }
          }
        }
      }
    }
  }

  @SneakyThrows
  public void materialFromBolting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barFromBolting.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barFromBolting.getProgress() >= 0.98) {
      barFromBolting.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barDown2.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barDown2.getProgress() >= 0.98) {
        barDown2.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barDown3.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barDown3.getProgress() >= 0.98) {
          barDown3.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barDown4.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);

          if (barDown4.getProgress() >= 0.98) {
            barDown4.setProgress(0);
            progress = 0.1;
            for (int i = 0; i < 10; i++) {
              barDown5.setProgress(progress);
              progress += 0.1;
              Thread.sleep(animationSpeed);
            }
            Thread.sleep(waitingTime);

            if (barDown5.getProgress() >= 0.98) {
              barDown5.setProgress(0);
              progress = 0.1;
              for (int i = 0; i < 10; i++) {
                barDeparture2.setProgress(progress);
                progress += 0.1;
                Thread.sleep(animationSpeed);
              }
              Thread.sleep(waitingTime);
              barDeparture2.setProgress(0);
            }
          }
        }
      }
    }
  }

  @SneakyThrows
  public void materialFromRiveting() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barFromRiveting.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barFromRiveting.getProgress() >= 0.98) {
      barFromRiveting.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barDown3.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barDown3.getProgress() >= 0.98) {
        barDown3.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barDown4.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barDown4.getProgress() >= 0.98) {
          barDown4.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barDown5.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);

          if (barDown5.getProgress() >= 0.98) {
            barDown5.setProgress(0);
            progress = 0.1;
            for (int i = 0; i < 10; i++) {
              barDeparture2.setProgress(progress);
              progress += 0.1;
              Thread.sleep(animationSpeed);
            }
            Thread.sleep(waitingTime);
            barDeparture2.setProgress(0);
          }
        }
      }
    }
  }

  @SneakyThrows
  public void materialFromWelding() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barFromWelding.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barFromWelding.getProgress() >= 0.98) {
      barFromWelding.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barDown4.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barDown4.getProgress() >= 0.98) {
        barDown4.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barDown5.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barDown5.getProgress() >= 0.98) {
          barDown5.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barDeparture2.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);
          barDeparture2.setProgress(0);
        }
      }
    }
  }

  @SneakyThrows
  public void materialFromPosition() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barFromPosition.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barFromPosition.getProgress() >= 0.98) {
      barFromPosition.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barDown5.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barDown5.getProgress() >= 0.98) {
        barDown5.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barDeparture2.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);
        barDeparture2.setProgress(0);
      }
    }
  }

  @SneakyThrows
  public void materialToDeparture() {
    long animationSpeed = calculateAnimationSpeed();
    long waitingTime = calculateWaitingTime();

    double progress = 0.1;
    for (int i = 0; i < 10; i++) {
      barToFix.setProgress(progress);
      progress += 0.1;
      Thread.sleep(animationSpeed);
    }
    Thread.sleep(waitingTime);
    if (barToFix.getProgress() >= 0.98) {
      barToFix.setProgress(0);
      progress = 0.1;
      for (int i = 0; i < 10; i++) {
        barToFixDown1.setProgress(progress);
        progress += 0.1;
        Thread.sleep(animationSpeed);
      }
      Thread.sleep(waitingTime);

      if (barToFixDown1.getProgress() >= 0.98) {
        barToFixDown1.setProgress(0);
        progress = 0.1;
        for (int i = 0; i < 10; i++) {
          barToFixDown2.setProgress(progress);
          progress += 0.1;
          Thread.sleep(animationSpeed);
        }
        Thread.sleep(waitingTime);

        if (barToFixDown2.getProgress() >= 0.98) {
          barToFixDown2.setProgress(0);
          progress = 0.1;
          for (int i = 0; i < 10; i++) {
            barToFixDown3.setProgress(progress);
            progress += 0.1;
            Thread.sleep(animationSpeed);
          }
          Thread.sleep(waitingTime);

          if (barToFixDown3.getProgress() >= 0.98) {
            barToFixDown3.setProgress(0);
            progress = 0.1;
            for (int i = 0; i < 10; i++) {
              barDeparture1.setProgress(progress);
              progress += 0.1;
              Thread.sleep(animationSpeed);
            }
            Thread.sleep(waitingTime);

            if (barDeparture1.getProgress() >= 0.98) {
              barDeparture1.setProgress(0);
              progress = 0.1;
              for (int i = 0; i < 10; i++) {
                barDeparture2.setProgress(progress);
                progress += 0.1;
                Thread.sleep(animationSpeed);
              }
              Thread.sleep(waitingTime);
              barDeparture2.setProgress(0);
            }
          }
        }
      }
    }
  }
}
