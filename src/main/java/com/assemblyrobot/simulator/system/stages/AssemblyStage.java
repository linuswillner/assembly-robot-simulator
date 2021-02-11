package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stations.AssemblyStation;
import java.util.PriorityQueue;
import lombok.Getter;

public class AssemblyStage extends Stage {

  private final StageController stageController;
  @Getter private PriorityQueue<AssemblyStation> stageQueue = new PriorityQueue<>();
  @Getter private final StageID stageId = StageID.ASSEMBLY;
  private MaterialStationData stationData;

  public AssemblyStage(int stationAmount, StageController stageController){
    this.stageController = stageController;
    createStations(stationAmount);
  }

  private void createStations(int stationAmount){
    for(int i = 0; i < stationAmount; i++){
      stageQueue.add(new AssemblyStation(stageController));
    }
  }

  @Override
  public void addToQueue(Material material) {
    stationData = new MaterialStationData();
    stationData.setStageId(stageId);
    stageQueue.peek().addToQueue(material, stationData);
  }

  // Interface overload method
  @Override
  public void addToQueue(Material material, MaterialStationData stationData) {

  }

  public void onChildQueueDepart(Material material, MaterialStationData data) {

  }

}
