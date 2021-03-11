package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.assemblyrobot.simulator.system.stages.FixStage;
import com.assemblyrobot.ui.models.StationVisualization;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.val;

public class StationViewerController {
  /**
   * Used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link StationVisualization}. This one formats data from the {@link
   * AssemblyStage}.
   *
   * @return Returns an {@link ObservableList} that contains {@link StationVisualization} objects.
   */
  public ObservableList<StationVisualization> getAssemblyVisualizations() {
    ObservableList<StationVisualization> data = FXCollections.observableArrayList();

    val stations = AssemblyStage.getStationQueue().getAll();
    stations.sort(Comparator.comparingInt(this::getStationNumber));

    stations.forEach(
        station ->
            data.add(
                new StationVisualization(
                    station.getStationId(), station.isBusy(), station.getOnQueue())));

    return data;
  }

  /**
   * Used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link StationVisualization}. This one formats data from the {@link
   * ErrorCheckStage}.
   *
   * @return Returns an {@link ObservableList} that contains {@link StationVisualization} objects.
   */
  public ObservableList<StationVisualization> getErrorCheckVisualizations() {
    ObservableList<StationVisualization> data = FXCollections.observableArrayList();

    val stations = ErrorCheckStage.getStationQueue().getAll();
    stations.sort(Comparator.comparingInt(this::getStationNumber));

    stations.forEach(
        station ->
            data.add(
                new StationVisualization(
                    station.getStationId(), station.isBusy(), station.getOnQueue())));

    return data;
  }

  /**
   * Used to reformat {@link com.assemblyrobot.simulator.system.components.StationQueue} into a data
   * format usable by {@link StationVisualization}. This one formats data from the {@link FixStage}.
   *
   * @return Returns an {@link ObservableList} that contains {@link StationVisualization} objects.
   */
  public ObservableList<StationVisualization> getFixVisualizations() {
    ObservableList<StationVisualization> data = FXCollections.observableArrayList();

    val queues = FixStage.getSubstations().values();
    val stations = new ArrayList<Station>();
    queues.forEach(stationQueue -> stations.addAll(stationQueue.getAll()));
    stations.sort(Comparator.comparing(Station::getStationId));

    stations.forEach(
        station ->
            data.add(
                new StationVisualization(
                    station.getStationId(), station.isBusy(), station.getOnQueue())));

    return data;
  }

  /**
   * A helper method used to separate {@link Station#getStationId()} number from the name.
   *
   * @param station {@link Station#getStationId()} id is needed.
   * @return {@link Integer}
   */
  private int getStationNumber(Station station) {
    // The last element of the dash-separated ID is the number of this station
    val id = Iterables.getLast(Arrays.asList(station.getStationId().split("-")));
    return Integer.parseInt(id);
  }
}
