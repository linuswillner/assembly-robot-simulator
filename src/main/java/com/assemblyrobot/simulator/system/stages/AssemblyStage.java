package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stations.AssemblyStation;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;

public class AssemblyStage extends Stage {

  @Getter private final StageController stageController;
  private final PriorityQueue<AssemblyStation> stationQueue = new PriorityQueue<>();

  public AssemblyStage(int stationAmount, @NonNull StageController stageController) {
    this.stageController = stageController;
    createStations(stationAmount);
  }

  protected void createStations(int stationAmount) {
    for (int i = 0; i < stationAmount; i++) {
      stationQueue.add(new AssemblyStation(this));
    }
  }

  public void addToStationQueue(@NonNull Material material) {
    val stationData = new MaterialStationData();
    stationData.setStageId(StageID.ASSEMBLY);

    val nextFreeStation = stationQueue.peek();
    if (nextFreeStation != null) {
      nextFreeStation.addToStationQueue(material, stationData);
    }
  }
}
