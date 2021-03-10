package com.assemblyrobot.ui.views;

import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.MaterialDTO;
import com.assemblyrobot.shared.db.model.RunDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import com.assemblyrobot.shared.db.model.StationDTO;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.DatabaseViewerController;
import com.assemblyrobot.ui.models.ErrorFixTimeVisualization;
import com.assemblyrobot.ui.models.ErrorOccurrenceVisualization;
import com.assemblyrobot.ui.models.NormalDistributionVisualization;
import com.assemblyrobot.ui.models.StationAmountVisualization;
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
  @FXML private TableView<EngineDTO> engineTable;
  @FXML private TableView<StageControllerDTO> stageControllerTable;
  @FXML private TableView<StationDTO> stationTable;
  @FXML private TableView<MaterialDTO> materialTable;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Update run data
    controller.updateRuns();
    updateCurrentRun(0);

    // Populate run selector list
    ObservableList<String> items = FXCollections.observableArrayList();

    val runNames =
        controller.getRuns().stream()
            .map(runDTO -> "Run %d".formatted(runDTO.getId()))
            .sorted()
            .toArray(String[]::new);

    items.addAll(runNames);
    runList.setItems(items);
    runList.setValue(runNames[0]);

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
  }
}
