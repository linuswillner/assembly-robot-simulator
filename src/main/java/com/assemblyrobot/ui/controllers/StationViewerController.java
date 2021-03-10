package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.assemblyrobot.simulator.system.stages.FixStage;
import com.assemblyrobot.ui.models.TableData;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.val;

public class StationViewerController {

  /**
   * used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link TableData}. Each Stage has their own queue so there are 3 similar methods.
   *
   * @return returns a {@link ObservableList} that contains {@link TableData} objects for UI
   */
  public ObservableList<TableData> assemblyGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();

    val stations = AssemblyStage.getStationQueue().getAll();
    stations.sort(Comparator.comparingInt(this::getStationNumber));

    stations.forEach(station -> {
      data.add(new TableData(station.getStationId(), station.isBusy(), station.getOnQueue()));
    });
    return data;
  }

  /**
   * used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link TableData}. Each Stage has their own queue so there are 3 similar methods.
   *
   * @return returns a {@link ObservableList} that contains {@link TableData} objects for UI
   */
  public ObservableList<TableData> errorCheckGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();

    val stations = ErrorCheckStage.getStationQueue().getAll();
    stations.sort(Comparator.comparingInt(this::getStationNumber));

    stations.forEach(station -> {
      data.add(new TableData(station.getStationId(), station.isBusy(), station.getOnQueue()));
    });
    return data;
  }

  /**
   * used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link TableData}. Each Stage has their own queue so there are 3 similar methods.
   *
   * @return returns a {@link ObservableList} that contains {@link TableData} objects for UI
   */
  public ObservableList<TableData> fixGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();

    val queues = FixStage.getSubstations().values();
    val stations = new ArrayList<Station>();
    queues.forEach(
        stationQueue -> stations.addAll(stationQueue.getAll()));
    stations.sort(Comparator.comparing(Station::getStationId));

    stations.forEach(station -> {
      data.add(new TableData(station.getStationId(), station.isBusy(), station.getOnQueue()));
    });
    return data;
  }

  /**
   * A helper method used to separate the station id number from the name.
   * @param station thats id is needed.
   * @return the station id number in integer format
   */
  private int getStationNumber(Station station) {
    // The last element of the dash-separated ID is the number of this station
    val id = Iterables.getLast(Arrays.asList(station.getStationId().split("-")));
    return Integer.parseInt(id);
  }
}
