package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.controllers.StageController;
import java.util.PriorityQueue;
import lombok.Getter;

public abstract class Stage {

  protected abstract void createStations(int stationAmount);

  public abstract void addToStationQueue(Material material);
}
