package com.assemblyrobot.ui.views;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.ui.Main;
import com.assemblyrobot.ui.controllers.StationViewerController;
import com.assemblyrobot.ui.models.TableData;
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

  @Setter
  private Main main;
  //AssemblyStage columns
  @FXML
  private TableView<TableData> TableAssemblyStage;
  @FXML
  private TableColumn<TableData, String> TableColumnAssemblyStatus;
  @FXML
  private TableColumn<TableData, String> TableColumnAssemblyName;
  @FXML
  private TableColumn<TableData, String> TableColumnAssemblyQ;


  //ErrorCheck columns
  @FXML
  private TableView<TableData> TableErrorCheckStage;
  @FXML
  private TableColumn<TableData, String> TableColumnErrorCheckStatus;
  @FXML
  private TableColumn<TableData, String> TableColumnErrorCheckName;
  @FXML
  private TableColumn<TableData, String> TableColumnErrorCheckQ;

  //FixColumns
  @FXML
  private TableView<TableData> TableFixStage;
  @FXML
  private TableColumn<TableData, String> TableColumnFixStatus;
  @FXML
  private TableColumn<TableData, String> TableColumnFixName;
  @FXML
  private TableColumn<TableData, String> TableColumnFixQ;

  StationViewerController sv = new StationViewerController();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //AssemblyStage columns
    TableColumnAssemblyStatus
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("status"));
    TableColumnAssemblyName
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("name"));
    TableColumnAssemblyQ
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("queueLength"));

    //ErrorCheckStage columns
    TableColumnErrorCheckStatus
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("status"));
    TableColumnErrorCheckName
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("name"));
    TableColumnErrorCheckQ
        .setCellValueFactory(new PropertyValueFactory<TableData, String>("queueLength"));

    //FixStage columns
    TableColumnFixStatus.setCellValueFactory(new PropertyValueFactory<TableData, String>("status"));
    TableColumnFixName.setCellValueFactory(new PropertyValueFactory<TableData, String>("name"));
    TableColumnFixQ.setCellValueFactory(new PropertyValueFactory<TableData, String>("queueLength"));

    //load the data

    refresh();


  }

  /**
   * Updates the tables in the UI. called when simulation time advances and when the simulation is
   * first loaded.
   */
  private void refresh() {
    TableAssemblyStage.setItems(sv.assemblyGetData());
    TableErrorCheckStage.setItems(sv.errorCheckGetData());
    TableFixStage.setItems(sv.fixGetData());
  }

  @Override
  public void setStage(Stage stage) {
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
