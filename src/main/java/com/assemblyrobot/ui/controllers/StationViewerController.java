package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.assemblyrobot.simulator.system.stages.FixStage;
import com.assemblyrobot.ui.models.StationVisualization;
import com.assemblyrobot.ui.views.StationViewer;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import lombok.val;

/** Controller class for the {@link com.assemblyrobot.ui.views.StationViewer}. */
@RequiredArgsConstructor
public class StationViewerController {
  private final StationViewer stationViewer;

  public void setEngine(Engine engine) {
    engine.setStationViewerController(this);
  }

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
                    station.getId(), station.isBusy(), station.getQueueLength())));

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
                    station.getId(), station.isBusy(), station.getQueueLength())));

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
    stations.sort(Comparator.comparing(Station::getId));

    stations.forEach(
        station ->
            data.add(
                new StationVisualization(
                    station.getId(), station.isBusy(), station.getQueueLength())));

    return data;
  }

  public void refreshStationViewer() {
    stationViewer.refresh();
  }

  public void resetStationViewer() {
    stationViewer.reset();
  }

  /**
   * A helper method used to separate {@link Station#getId()} number from the name.
   *
   * @param station {@link Station#getId()} id is needed.
   * @return {@link Integer}
   */
  private int getStationNumber(Station station) {
    // The last element of the dash-separated ID is the number of this station
    val id = Iterables.getLast(Arrays.asList(station.getId().split("-")));
    return Integer.parseInt(id);
  }
}
