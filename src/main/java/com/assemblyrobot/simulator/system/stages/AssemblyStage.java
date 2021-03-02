package com.assemblyrobot.simulator.system.stages;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.StationConfig;
import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Stage;
import com.assemblyrobot.simulator.system.components.StageController;
import com.assemblyrobot.simulator.system.stations.AssemblyStation;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

public class AssemblyStage extends Stage {

  @Getter private final StageController stageController;
  private final PriorityQueue<AssemblyStation> stationQueue = new PriorityQueue<>();
  private final StationConfig config = Config.getConfig().getStationParams();

  public AssemblyStage(@NonNull StageController stageController) {
    this.stageController = stageController;
    createStations();
  }

  @Override
  protected void createStations() {
    for (int i = 0; i < config.getAssemblyStationAmount(); i++) {
      stationQueue.add(new AssemblyStation(this));
    }
  }

  @Override
  public void addToStationQueue(@NonNull Material material) {
    val nextFreeStation = stationQueue.peek();
    if (nextFreeStation != null) {
      nextFreeStation.addToStationQueue(material, StageID.ASSEMBLY);
    }
  }
}
