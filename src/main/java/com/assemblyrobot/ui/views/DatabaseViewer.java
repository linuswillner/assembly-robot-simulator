package com.assemblyrobot.ui.views;

import com.assemblyrobot.shared.db.model.MaterialDTO;
import com.assemblyrobot.shared.db.model.RunDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import com.assemblyrobot.shared.db.model.StationDTO;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.DatabaseViewerController;
import com.assemblyrobot.ui.models.EngineVisualization;
import com.assemblyrobot.ui.models.ErrorFixTimeVisualization;
import com.assemblyrobot.ui.models.ErrorOccurrenceVisualization;
import com.assemblyrobot.ui.models.NormalDistributionVisualization;
import com.assemblyrobot.ui.models.StationAmountVisualization;
import com.assemblyrobot.ui.models.StationVisualization;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.val;

/* Backend logic component for DatabaseViewer.fxml. */
public class DatabaseViewer implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;

  private final DatabaseViewerController controller = new DatabaseViewerController();
  private RunDTO currentRun;

  @FXML private ChoiceBox<String> runList;
  @FXML private TableView<NormalDistributionVisualization> normalDistributionTable;
  @FXML private TableView<ErrorOccurrenceVisualization> errorOccurrenceTable;
  @FXML private TableView<ErrorFixTimeVisualization> errorFixTimeTable;
  @FXML private TableView<StationAmountVisualization> stationAmountTable;
  @FXML private TableView<EngineVisualization> engineTable;
  @FXML private TableView<StageControllerDTO> stageControllerTable;
  @FXML private TableView<StationVisualization> stationTable;
  @FXML private TableView<MaterialDTO> materialTable;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Update run data
    controller.updateRuns();
    val latestRun = controller.getRuns().size() - 1;
    updateCurrentRun(latestRun); // Always set the latest as active

    // Populate run selector list
    ObservableList<String> items = FXCollections.observableArrayList();

    val runNames =
        controller.getRuns().stream()
            .map(runDTO -> "Run %d".formatted(runDTO.getId()))
            .sorted()
            .toArray(String[]::new);

    items.addAll(runNames);
    runList.setItems(items);
    runList.setValue(runNames[latestRun]);

    // Add selection listener for current run
    runList
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener((observable, oldValue, newValue) -> updateCurrentRun((int) newValue));

    // Prep tables

    // Normal distribution table
    val normalColumns = normalDistributionTable.getColumns();
    normalColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("setting"));
    normalColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("mean"));
    normalColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("variance"));

    // Error occurrence table
    val occurrenceColumns = errorOccurrenceTable.getColumns();
    occurrenceColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("setting"));
    occurrenceColumns
        .get(1)
        .setCellValueFactory(new PropertyValueFactory<>("typeDistributionMean"));
    occurrenceColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("errorThreshold"));

    // Error fix time table
    val fixTimeColumns = errorFixTimeTable.getColumns();
    fixTimeColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("errorType"));
    fixTimeColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("fixTime"));

    // Station amount table
    val stationAmountColumns = stationAmountTable.getColumns();
    stationAmountColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("stationType"));
    stationAmountColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));

    // Engine table
    val engineColumns = engineTable.getColumns();
    engineColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("totalSimulationTime"));
    engineColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("totalThroughput"));

    // Stage controller table
    val stageControllerColumns = stageControllerTable.getColumns();
    stageControllerColumns
        .get(0)
        .setCellValueFactory(new PropertyValueFactory<>("totalEnteredMaterialAmount"));
    stageControllerColumns
        .get(1)
        .setCellValueFactory(new PropertyValueFactory<>("totalExitedMaterialAmount"));

    // Stations table
    val stationColumns = stationTable.getColumns();
    stationColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
    stationColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("enteredMaterialAmount"));
    stationColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("exitedMaterialAmount"));
    stationColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("busyTime"));
    stationColumns.get(4).setCellValueFactory(new PropertyValueFactory<>("totalPassThroughTime"));
    stationColumns.get(5).setCellValueFactory(new PropertyValueFactory<>("utilization"));
    stationColumns.get(6).setCellValueFactory(new PropertyValueFactory<>("throughput"));
    stationColumns.get(7).setCellValueFactory(new PropertyValueFactory<>("averageServiceTime"));
    stationColumns.get(8).setCellValueFactory(new PropertyValueFactory<>("averageResponseTime"));
    stationColumns.get(9).setCellValueFactory(new PropertyValueFactory<>("averageQueueLength"));

    // Materials table
    val materialColumns = materialTable.getColumns();
    materialColumns.get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
    materialColumns.get(1).setCellValueFactory(new PropertyValueFactory<>("queueDuration"));
    materialColumns.get(2).setCellValueFactory(new PropertyValueFactory<>("processingDuration"));
    materialColumns.get(3).setCellValueFactory(new PropertyValueFactory<>("passthroughTime"));
  }

  private void updateCurrentRun(int newIndex) {
    // Update current run
    currentRun = controller.getRuns().get(newIndex);

    // Clear tables
    normalDistributionTable.getItems().clear();
    errorOccurrenceTable.getItems().clear();
    errorFixTimeTable.getItems().clear();
    stationAmountTable.getItems().clear();
    engineTable.getItems().clear();
    stageControllerTable.getItems().clear();
    stationTable.getItems().clear();
    materialTable.getItems().clear();

    // Populate table
    populateTable();
  }

  private void populateTable() {
    // Normal distributions
    val normalItems = normalDistributionTable.getItems();
    normalItems.add(
        new NormalDistributionVisualization(
            "Arrival interval", currentRun.getArrivalIntervalParams()));
    normalItems.add(
        new NormalDistributionVisualization("Assembly time", currentRun.getAssemblyTimeParams()));
    normalItems.add(
        new NormalDistributionVisualization(
            "Error check time", currentRun.getErrorCheckTimeParams()));

    // Error occurrences
    val occurrenceItems = errorOccurrenceTable.getItems();
    occurrenceItems.add(
        new ErrorOccurrenceVisualization(
            "Error occurrence", currentRun.getErrorOccurrenceParams()));

    // Error fix times
    val fixTimes = currentRun.getErrorFixTimes();
    val fixTimeItems = errorFixTimeTable.getItems();
    fixTimeItems.add(new ErrorFixTimeVisualization("Fitting", fixTimes.getFittingFixTime()));
    fixTimeItems.add(new ErrorFixTimeVisualization("Bolting", fixTimes.getBoltingFixTime()));
    fixTimeItems.add(new ErrorFixTimeVisualization("Riveting", fixTimes.getRivetingFixTime()));
    fixTimeItems.add(new ErrorFixTimeVisualization("Welding", fixTimes.getWeldingFixTime()));
    fixTimeItems.add(new ErrorFixTimeVisualization("Returning", fixTimes.getReturningFixTime()));

    // Station amounts
    val stationAmounts = currentRun.getStationParams();
    val stationAmountItems = stationAmountTable.getItems();
    stationAmountItems.add(
        new StationAmountVisualization("Assembly", stationAmounts.getAssemblyStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization("Error check", stationAmounts.getErrorCheckStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization(
            "Fix: Fitting", stationAmounts.getFittingFixStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization(
            "Fix: Bolting", stationAmounts.getBoltingFixStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization(
            "Fix: Riveting", stationAmounts.getRivetingFixStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization(
            "Fix: Welding", stationAmounts.getWeldingFixStationAmount()));
    stationAmountItems.add(
        new StationAmountVisualization(
            "Fix: Returning", stationAmounts.getReturningFixStationAmount()));

    // Engines
    engineTable
        .getItems()
        .add(new EngineVisualization(currentRun.getEngine(), currentRun.getStageController()));

    // Stage controllers
    stageControllerTable.getItems().add(currentRun.getStageController());

    // Stations table
    val stations = currentRun.getStations();
    val stationItems = stationTable.getItems();

    for (StationDTO stationDTO : stations) {
      stationItems.add(new StationVisualization(stationDTO, currentRun.getEngine()));
    }

    // Materials table
    val materials = currentRun.getMaterials();
    materialTable.getItems().addAll(materials);
  }

  @Override
  public void afterInitialize() {}
}
