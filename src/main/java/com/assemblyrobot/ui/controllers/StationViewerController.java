package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.FixStage;
import com.assemblyrobot.ui.models.TableData;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.val;

public class StationViewerController {


  public ObservableList<TableData> assemblyGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();
    //Collections.sort(AssemblyStage.getStationQueue().getLast(id.split("-")));

    data.add(new TableData("1", "Busy", 5));
    return data;
  }

  public ObservableList<TableData> errorCheckGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();
    data.add(new TableData("1", "Busy", 5));
    return data;
  }

  public ObservableList<TableData> fixGetData() {
    ObservableList<TableData> data = FXCollections.observableArrayList();
    data.add(new TableData("1", "Busy", 5));
    return data;
  }

  private List<Station> iterateList(List<Station> list) {
    list.forEach(station -> {
          val i = Iterables.getLast(Arrays.asList(station.getStationId().split("-")));
        }
    );
  }


}
