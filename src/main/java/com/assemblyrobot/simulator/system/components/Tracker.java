package com.assemblyrobot.simulator.system.components;

import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Tracker {

  @Getter private final long trackerId;

  @Getter private final ArrayList<MaterialStationData> dataForStations = new ArrayList<>();

  public void addData(@NonNull MaterialStationData data) {
    dataForStations.add(data);
  }
}
