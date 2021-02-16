package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import java.util.ArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class Tracker {


  //TODO Figure out a way to match this with Stage controller
  @Getter
  private final ArrayList<MaterialStationData> dataForStations = new ArrayList<MaterialStationData>();

  //this is for testing with custom data
  public void addData(MaterialStationData data){
    dataForStations.add(data);
  }


}
