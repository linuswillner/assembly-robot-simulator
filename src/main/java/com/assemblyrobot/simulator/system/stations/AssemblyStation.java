package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import lombok.NonNull;

/** {@link Station} implementation for the {@link AssemblyStage}. */
public class AssemblyStation extends Station implements Comparable<AssemblyStation> {
  private static int nextFreeId = 1;

  public AssemblyStation(@NonNull AssemblyStage stage) {
    super(stage.getStageController(), "AssemblyStation-%d".formatted(nextFreeId));
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }

  @Override
  public int compareTo(@NonNull AssemblyStation station) {
    return Integer.compare(this.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
