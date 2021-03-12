package com.assemblyrobot.ui.models;

import com.assemblyrobot.shared.db.model.EngineDTO;
import com.assemblyrobot.shared.db.model.StationDTO;
import lombok.Getter;
import lombok.val;

public class StationVisualization {
  @Getter private final String id;
  @Getter private final double enteredMaterialAmount;
  @Getter private final double exitedMaterialAmount;
  @Getter private final double busyTime;
  @Getter private final String utilization;
  @Getter private final double throughput;
  @Getter private final double averageServiceTime;
  @Getter private final double totalPassThroughTime;
  @Getter private final double averageResponseTime;
  @Getter private final double averageQueueLength;

  public StationVisualization(StationDTO stationDTO, EngineDTO engineDTO) {
    val totalSimulationTime = engineDTO.getTotalSimulationTime();

    id = stationDTO.getId();
    enteredMaterialAmount = stationDTO.getEnteredMaterialAmount();
    exitedMaterialAmount = stationDTO.getExitedMaterialAmount();
    busyTime = stationDTO.getBusyTime();
    utilization = "%.2f%%".formatted(isDivisible(busyTime * 100, totalSimulationTime));
    throughput = isDivisible(exitedMaterialAmount, totalSimulationTime);
    averageServiceTime = isDivisible(busyTime, exitedMaterialAmount);
    totalPassThroughTime = stationDTO.getTotalPassthroughTime();
    averageResponseTime = isDivisible(totalPassThroughTime, exitedMaterialAmount);
    averageQueueLength = isDivisible(totalPassThroughTime, totalSimulationTime);
  }

  private double isDivisible(double firstValue, double secondValue) {
    if (firstValue != 0 && secondValue != 0) {
      return firstValue / secondValue;
    }
    return 0.0;
  }
}
