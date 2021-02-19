package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import lombok.NonNull;

public class AssemblyStation extends Station implements Comparable<AssemblyStation> {
  public AssemblyStation(@NonNull AssemblyStage stage) {
    super(stage.getStageController());
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }

  // Handles ordering the PriorityQueue inside AssemblyStage
  @Override
  public int compareTo(@NonNull AssemblyStation station) {
    return Integer.compare(super.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
