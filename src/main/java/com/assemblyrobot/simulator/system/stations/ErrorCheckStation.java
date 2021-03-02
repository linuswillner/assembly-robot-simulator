package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.ErrorCheckTimeGenerator;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import lombok.NonNull;

public class ErrorCheckStation extends Station implements Comparable<ErrorCheckStation> {
  private static int nextFreeId = 1;

  public ErrorCheckStation(@NonNull ErrorCheckStage stage) {
    super(stage.getStageController(), "ErrorCheckStation-%d".formatted(nextFreeId));
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }

  @Override
  protected long getProcessingTime() {
    return ErrorCheckTimeGenerator.getInstance().nextLong();
  }

  @Override
  public int compareTo(@NonNull ErrorCheckStation station) {
    return Integer.compare(super.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
