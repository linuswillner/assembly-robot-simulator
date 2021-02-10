package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.metrics.MaterialStationData;
import java.util.PriorityQueue;
import lombok.Getter;

public abstract class Stage implements BiDirectionalQueueable{

  @Getter private PriorityQueue<Station> stations;

  @Override
  public void addToQueue(Material material) {

  }

  @Override
  public void onChildQueueDepart(Material material, MaterialStationData data) {

  }
}
