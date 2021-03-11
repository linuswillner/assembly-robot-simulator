package com.assemblyrobot.ui.views;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.StationViewerController;
import com.assemblyrobot.ui.models.StationVisualization;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;

public class StationViewer extends TickAdvanceListener implements Initializable, View {
  @Setter private Main main;
  @Setter private Stage stage;

  // AssemblyStage columns
  @FXML private TableView<StationVisualization> assemblyStageTable;
  @FXML private TableColumn<StationVisualization, String> assemblyStatusColumn;
  @FXML private TableColumn<StationVisualization, String> assemblyNameColumn;
  @FXML private TableColumn<StationVisualization, String> assemblyQueueColumn;

  // ErrorCheck columns
  @FXML private TableView<StationVisualization> errorCheckStageTable;
  @FXML private TableColumn<StationVisualization, String> errorCheckStatusColumn;
  @FXML private TableColumn<StationVisualization, String> errorCheckNameColumn;
  @FXML private TableColumn<StationVisualization, String> errorCheckQueueColumn;

  // FixColumns
  @FXML private TableView<StationVisualization> fixStageTable;
  @FXML private TableColumn<StationVisualization, String> fixStatusColumn;
  @FXML private TableColumn<StationVisualization, String> fixNameColumn;
  @FXML private TableColumn<StationVisualization, String> fixQueueColumn;

  StationViewerController stationViewerController = new StationViewerController();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // AssemblyStage columns
    assemblyStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    assemblyNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    assemblyQueueColumn.setCellValueFactory(new PropertyValueFactory<>("queueLength"));

    // ErrorCheckStage columns
    errorCheckStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    errorCheckNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    errorCheckQueueColumn.setCellValueFactory(new PropertyValueFactory<>("queueLength"));

    // FixStage columns
    fixStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    fixNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    fixQueueColumn.setCellValueFactory(new PropertyValueFactory<>("queueLength"));

    // Load data
    refresh();
  }

  private void refresh() {
    assemblyStageTable.setItems(stationViewerController.getAssemblyVisualizations());
    errorCheckStageTable.setItems(stationViewerController.getErrorCheckVisualizations());
    fixStageTable.setItems(stationViewerController.getFixVisualizations());
  }

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    refresh();
  }

  @Override
  protected void onTickReset() {
    refresh();
  }
}
