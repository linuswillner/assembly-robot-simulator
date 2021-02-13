package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stations.ErrorCheckStation;
import java.util.PriorityQueue;
import lombok.Getter;

public class ErrorCheckStage extends Stage {

  private final StageController stageController;
  @Getter private PriorityQueue<ErrorCheckStation> stageQueue = new PriorityQueue<>();
  @Getter private final StageID stageId = StageID.ERROR_CHECK;
  private MaterialStationData stationData;

  public ErrorCheckStage(int stationAmount, StageController stageController){
    this.stageController = stageController;
    createStations(stationAmount);
  }

  private void createStations(int stationAmount){
    for(int i = 0; i < stationAmount; i++){
      stageQueue.add(new ErrorCheckStation(stageController));
    }
  }

  public void addToErrorCheckStationQueue(Material material) {
    stationData = new MaterialStationData();
    stationData.setStageId(stageId);
    stageQueue.peek().addToStationQueue(material, stationData);
  }



  public void onChildQueueDepart(Material material, MaterialStationData data) {

  }

}
