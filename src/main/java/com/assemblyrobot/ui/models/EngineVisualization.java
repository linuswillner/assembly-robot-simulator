package com.assemblyrobot.ui.models;

import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.StageControllerDTO;
import lombok.Getter;

public class EngineVisualization {
  @Getter private final double totalSimulationTime;
  @Getter private final double exitedMaterialAmount;
  @Getter private final double totalThroughput;

  public EngineVisualization(EngineDTO engineDTO, StageControllerDTO stageControllerDTO) {

    exitedMaterialAmount = stageControllerDTO.getTotalExitedMaterialAmount();
    totalSimulationTime = engineDTO.getTotalSimulationTime();
    totalThroughput = isDivisible(exitedMaterialAmount, totalSimulationTime);
  }

  private double isDivisible(double firstValue, double secondValue) {
    if (firstValue != 0 && secondValue != 0) {
      return firstValue / secondValue;
    }
    return 0.0;
  }
}
