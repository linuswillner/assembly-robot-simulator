package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.controllers.StageController;
import java.util.PriorityQueue;
import lombok.Getter;

public abstract class Stage implements BiDirectionalQueueable{

  @Getter private final StageController stageController = new StageController();

  @Override
  public abstract void onChildQueueDepart(Material material, MaterialStationData data);

  public abstract PriorityQueue<Station> getStageQueue();

}

