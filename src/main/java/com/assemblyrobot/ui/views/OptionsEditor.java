package com.assemblyrobot.ui.views;

import com.assemblyrobot.ui.utils.TextFieldUtils;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class OptionsEditor implements Initializable {
  // Root container
  @FXML private AnchorPane settingsRoot;

  // Simulation settings
  @FXML private TextField arrivalIntervalAverage;
  @FXML private TextField arrivalIntervalVariance;
  @FXML private TextField assemblyTimeAverage;
  @FXML private TextField assemblyTimeVariance;
  @FXML private TextField errorCheckTimeAverage;
  @FXML private TextField errorCheckTimeVariance;
  @FXML private TextField errorOccurrenceCoefficient;
  @FXML private TextField errorOccurrenceThreshold;

  // Station amounts
  @FXML private TextField assemblyStationAmount;
  @FXML private TextField errorCheckStationAmount;
  @FXML private TextField fittingStationAmount;
  @FXML private TextField boltingStationAmount;
  @FXML private TextField rivetingStationAmount;
  @FXML private TextField weldingStationAmount;
  @FXML private TextField positionStationAmount;

  // Error fix times
  @FXML private TextField fittingFixTime;
  @FXML private TextField boltingFixTime;
  @FXML private TextField rivetingFixTime;
  @FXML private TextField weldingFixTime;
  @FXML private TextField positionFixTime;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Arrays.stream(TextFieldUtils.getAllTextFields(settingsRoot))
        .forEach(
            textField -> {
              // Require numeric values for all fields
              textField
                  .textProperty()
                  .addListener(
                      (observable, oldValue, newValue) -> {
                        if (!newValue.matches("\\d*")) {
                          textField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                      });
            });
  }

  @FXML
  public void handleLoadButtonClick() {
    // TODO
  }

  @FXML
  public void handleSaveButtonClick() {
    // TODO
  }

  @FXML
  public void handleResetButtonClick() {
    // TODO
  }
}
