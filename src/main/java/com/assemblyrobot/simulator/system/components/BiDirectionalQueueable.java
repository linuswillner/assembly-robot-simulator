package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;

public interface BiDirectionalQueueable {

  void addToQueue(Material material);
  void addToQueue(Material material, MaterialStationData stationData);
  void onChildQueueDepart(Material material, MaterialStationData stationData);

}
