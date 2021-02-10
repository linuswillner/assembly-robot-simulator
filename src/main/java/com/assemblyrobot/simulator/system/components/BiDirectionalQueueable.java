package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.metrics.MaterialStationData;

public interface BiDirectionalQueueable {

  void addToQueue(Material material);
  void onChildQueueDepart(Material material, MaterialStationData data);

}
