package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stations.AssemblyStation;
import java.util.PriorityQueue;
import lombok.Getter;

public class AssemblyStage extends Stage {

  @Getter private final StageController stageController;
  @Getter private PriorityQueue<AssemblyStation> stageQueue = new PriorityQueue<>();
  @Getter private final StageID stageId = StageID.ASSEMBLY;
  private MaterialStationData stationData;

  public AssemblyStage(int stationAmount, StageController stageController) {
    this.stageController = stageController;
    createStations(stationAmount);
  }

  protected void createStations(int stationAmount) {
    for (int i = 0; i < stationAmount; i++) {
      stageQueue.add(new AssemblyStation(this));
    }
  }

  public void addToStationQueue(Material material) {
    stationData = new MaterialStationData();
    stationData.setStageId(stageId);
    stageQueue.peek().addToStationQueue(material, stationData);
  }
}
