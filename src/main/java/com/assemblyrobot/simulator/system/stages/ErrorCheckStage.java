package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.simulator.system.components.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stations.ErrorCheckStation;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public class ErrorCheckStage extends Stage {

  @Getter private final StageController stageController;
  private final PriorityQueue<ErrorCheckStation> stationQueue = new PriorityQueue<>();

  public ErrorCheckStage(int stationAmount, @NonNull StageController stageController) {
    this.stageController = stageController;
    createStations(stationAmount);
  }

  @Override
  protected void createStations(int stationAmount) {
    for (int i = 0; i < stationAmount; i++) {
      stationQueue.add(new ErrorCheckStation(this));
    }
  }

  @Override
  public void addToStationQueue(@NonNull Material material) {
    val stationData = new MaterialStationData();
    stationData.setStageId(StageID.ERROR_CHECK);

    val nextFreeStation = stationQueue.peek();
    if (nextFreeStation != null) {
      nextFreeStation.addToStationQueue(material, stationData);
    }
  }
}
