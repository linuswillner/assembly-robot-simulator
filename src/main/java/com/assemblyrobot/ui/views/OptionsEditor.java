package com.assemblyrobot.ui.views;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.Config.UserSetting;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import com.assemblyrobot.shared.utils.JsonUtils;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.utils.AlertUtils;
import com.assemblyrobot.ui.utils.TextFieldUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptionsEditor implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;
  private static final Logger logger = LogManager.getLogger();

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
  @FXML private TextField returningStationAmount;

  // Error fix times
  @FXML private TextField fittingFixTime;
  @FXML private TextField boltingFixTime;
  @FXML private TextField rivetingFixTime;
  @FXML private TextField weldingFixTime;
  @FXML private TextField positionFixTime;

  // Misc
  @FXML private CheckBox errorCoefficientCheckbox;
  @FXML private Tooltip errorCoefficientTooltip;
  @FXML private Tooltip errorThresholdTooltip;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    val textFields = TextFieldUtils.getAllTextFields(settingsRoot);

    // Configure integer-only TextFields
    Arrays.stream(textFields)
        .filter(textField -> !textField.getId().equals("errorOccurrenceCoefficient"))
        .forEach(TextFieldUtils::configureIntegerOnlyTextField);

    // Configure the errorOccurrenceCoefficient field to accept decimals
    TextFieldUtils.configureDecimalOnlyTextField(errorOccurrenceCoefficient);

    // Make changes to any of the fields update the config
    Arrays.stream(textFields)
        .forEach(
            textField ->
                textField
                    .textProperty()
                    .addListener(
                        (observable, oldValue, newValue) -> {
                          // Only react to explicit changes
                          if (!oldValue.equals("") && !newValue.equals("")) {
                            updateConfig();
                          }
                        }));

    // Make the error coefficient checkbox mirror the disabled state of the related input
    errorCoefficientCheckbox.setOnAction(
        event -> errorOccurrenceCoefficient.setDisable(!errorOccurrenceCoefficient.isDisabled()));

    errorCoefficientTooltip.setShowDelay(new Duration(0));
    errorThresholdTooltip.setShowDelay(new Duration(0));

    // Pull initial config
    populateConfig();
  }

  @FXML
  public void handleLoadButtonClick() {
    val configFile = getFileChooser(null).showOpenDialog(main.getPrimaryStage());

    // Check if user actually picked a file
    if (configFile != null) {
      logger.debug("Loading custom config file {}.", configFile.getAbsolutePath());

      Config.putUserSetting(UserSetting.CUSTOM_CONFIG_PATH, configFile.getAbsolutePath());
      Config.load();

      if (Config.getUserSettingBoolean(UserSetting.CUSTOM_CONFIG_LOADED)) {
        AlertUtils.showAlert(
            AlertType.INFORMATION, "Custom config loaded", "Custom config successfully loaded.");
      } else {
        AlertUtils.showAlert(
            AlertType.ERROR,
            "Custom config loading failed",
            "An error occurred during the loading of the custom config!");
      }

      populateConfig();
    }
  }

  @FXML
  public void handleSaveButtonClick() {
    val configFile = getFileChooser("config.json").showSaveDialog(main.getPrimaryStage());

    // Check if user actually picked a location to save
    if (configFile != null) {
      val configPath = configFile.getAbsolutePath();
      logger.debug("Saving custom config file {}.", configPath);

      Config.putUserSetting(UserSetting.CUSTOM_CONFIG_PATH, configPath);

      try (val writer = new FileWriter(configPath)) {
        writer.write(JsonUtils.gson.toJson(Config.getConfig()));
        AlertUtils.showAlert(
            AlertType.INFORMATION,
            "Custom config saved",
            "Custom config saved successfully to %s.".formatted(configPath));
      } catch (IOException e) {
        AlertUtils.showExceptionAlert(
            "Custom config saving failed",
            "An error occurred during the saving of the custom config to %s!".formatted(configPath),
            e);
      }
    }
  }

  @FXML
  public void handleResetButtonClick() {
    Config.removeUserSetting(UserSetting.CUSTOM_CONFIG_PATH);
    Config.removeUserSetting(UserSetting.CUSTOM_CONFIG_LOADED);
    Config.load();
    populateConfig();
  }

  private FileChooser getFileChooser(String defaultFileName) {
    val fileChooser = new FileChooser();
    fileChooser.setInitialFileName(defaultFileName);
    val extensionFilter = new ExtensionFilter("JSON files (*.json)", "*.json");
    fileChooser.getExtensionFilters().add(extensionFilter);
    return fileChooser;
  }

  private void populateConfig() {
    logger.debug("Populating config.");

    // Populate configuration into the fields
    val config = Config.getConfig();

    // Simulation settings

    setMeanAndVariance(
        arrivalIntervalAverage, arrivalIntervalVariance, config.getArrivalIntervalParams());
    setMeanAndVariance(assemblyTimeAverage, assemblyTimeVariance, config.getAssemblyTimeParams());
    setMeanAndVariance(
        errorCheckTimeAverage, errorCheckTimeVariance, config.getErrorCheckTimeParams());

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
    setStationAmount(returningStationAmount, stationParams.getReturningFixStationAmount());

    // Error fix times

    val fixTimes = config.getErrorFixTimes();
    setFixTime(fittingFixTime, fixTimes.getFittingFixTime());
    setFixTime(boltingFixTime, fixTimes.getBoltingFixTime());
    setFixTime(rivetingFixTime, fixTimes.getRivetingFixTime());
    setFixTime(weldingFixTime, fixTimes.getWeldingFixTime());
    setFixTime(positionFixTime, fixTimes.getReturningFixTime());
  }

  private void updateConfig() {
    logger.debug("Updating config.");

    // Updates the config based on values set in the options editor
    val config = Config.getConfig();

    // Simulation settings

    getMeanAndVariance(
        arrivalIntervalAverage, arrivalIntervalVariance, config.getArrivalIntervalParams());
    getMeanAndVariance(assemblyTimeAverage, assemblyTimeVariance, config.getAssemblyTimeParams());
    getMeanAndVariance(
        errorCheckTimeAverage, errorCheckTimeVariance, config.getErrorCheckTimeParams());

    config
        .getErrorOccurrenceParams()
        .setTypeDistributionMean(Double.parseDouble(errorOccurrenceCoefficient.getText()));
    config
        .getErrorOccurrenceParams()
        .setErrorThreshold(Integer.parseInt(errorOccurrenceThreshold.getText()));

    // Station amounts

    val stationParams = config.getStationParams();
    stationParams.setAssemblyStationAmount(getStationAmount(assemblyStationAmount));
    stationParams.setErrorCheckStationAmount(getStationAmount(errorCheckStationAmount));
    stationParams.setFittingFixStationAmount(getStationAmount(fittingStationAmount));
    stationParams.setBoltingFixStationAmount(getStationAmount(boltingStationAmount));
    stationParams.setRivetingFixStationAmount(getStationAmount(rivetingStationAmount));
    stationParams.setWeldingFixStationAmount(getStationAmount(weldingStationAmount));
    stationParams.setReturningFixStationAmount(getStationAmount(returningStationAmount));

    // Error fix times

    val fixTimes = config.getErrorFixTimes();
    fixTimes.setFittingFixTime(getFixTime(fittingFixTime));
    fixTimes.setBoltingFixTime(getFixTime(boltingFixTime));
    fixTimes.setRivetingFixTime(getFixTime(rivetingFixTime));
    fixTimes.setWeldingFixTime(getFixTime(weldingFixTime));
    fixTimes.setReturningFixTime(getFixTime(positionFixTime));

    Config.updateConfig(config);
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

  private void getMeanAndVariance(
      TextField meanField, TextField varianceField, NormalDistributionConfig config) {
    config.setMean(Integer.parseInt(meanField.getText()));
    config.setVariance(Integer.parseInt(varianceField.getText()));
  }

  private int getStationAmount(TextField amountField) {
    return Integer.parseInt(amountField.getText());
  }

  private int getFixTime(TextField fixTimeField) {
    return Integer.parseInt(fixTimeField.getText());
  }
}
