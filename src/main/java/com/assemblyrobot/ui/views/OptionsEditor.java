package com.assemblyrobot.ui.views;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import com.assemblyrobot.ui.utils.TextFieldUtils;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.val;

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
    // Configure integer-only TextFields
    Arrays.stream(TextFieldUtils.getAllTextFields(settingsRoot))
        .filter(textField -> !textField.getId().equals("errorOccurrenceCoefficient"))
        .forEach(TextFieldUtils::configureIntegerOnlyTextField);

    // Configure the errorOccurrenceCoefficient field to accept decimals
    TextFieldUtils.configureDecimalOnlyTextField(errorOccurrenceCoefficient);

    // Populate configuration into the fields
    val config = Config.getConfig();

    // Simulation settings

    // Arrival interval
    setMeanAndVariance(
        arrivalIntervalAverage, arrivalIntervalVariance, config.getArrivalIntervalParams());

    // Assembly time
    setMeanAndVariance(assemblyTimeAverage, assemblyTimeVariance, config.getAssemblyTimeParams());

    // Error check time
    setMeanAndVariance(
        errorCheckTimeAverage, errorCheckTimeVariance, config.getErrorCheckTimeParams());

    // Error occurrences
    errorOccurrenceCoefficient.setText(
        Double.toString(config.getErrorOccurrenceParams().getTypeDistributionMean()));
    errorOccurrenceThreshold.setText(
        Integer.toString(config.getErrorOccurrenceParams().getErrorThreshold()));

    // Station amounts

    val stationParams = config.getStationParams();
    setStationAmount(assemblyStationAmount, stationParams.getAssemblyStationAmount());
    setStationAmount(errorCheckStationAmount, stationParams.getErrorCheckStationAmount());
    setStationAmount(fittingStationAmount, stationParams.getFittingFixStationAmount());
    setStationAmount(boltingStationAmount, stationParams.getBoltingFixStationAmount());
    setStationAmount(rivetingStationAmount, stationParams.getRivetingFixStationAmount());
    setStationAmount(weldingStationAmount, stationParams.getWeldingFixStationAmount());
    setStationAmount(positionStationAmount, stationParams.getReturningFixStationAmount());

    // Error fix times
    val fixTimes = config.getErrorFixTimes();
    setFixTime(fittingFixTime, fixTimes.getFittingFixTime());
    setFixTime(boltingFixTime, fixTimes.getBoltingFixTime());
    setFixTime(rivetingFixTime, fixTimes.getRivetingFixTime());
    setFixTime(weldingFixTime, fixTimes.getWeldingFixTime());
    setFixTime(positionFixTime, fixTimes.getReturningFixTime());
  }

  private void setMeanAndVariance(
      TextField meanField, TextField varianceField, NormalDistributionConfig config) {
    meanField.setText(Integer.toString(config.getMean()));
    varianceField.setText(Integer.toString(config.getVariance()));
  }

  private void setStationAmount(TextField amountField, int stationAmount) {
    amountField.setText(Integer.toString(stationAmount));
  }

  private void setFixTime(TextField fixTimeField, int time) {
    fixTimeField.setText(Integer.toString(time));
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
