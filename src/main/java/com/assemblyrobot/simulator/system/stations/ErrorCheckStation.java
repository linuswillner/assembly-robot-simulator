package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import lombok.NonNull;

public class ErrorCheckStation extends Station implements Comparable<ErrorCheckStation> {
  public ErrorCheckStation(@NonNull ErrorCheckStage stage) {
    super(stage.getStageController());
  }

  // TODO: Note this is a placeholder method. Change after all time generators have been
  // implemented.
  @Override
  protected long getProcessingTime() {
    return 5;
  }
  /* @Override
  protected long getProcessingTime() {
    return ErrorCheckTimeGenerator.getInstance().nextLong();
  }*/

  // Handles ordering the PriorityQueue inside AssemblyStage
  @Override
  public int compareTo(@NonNull ErrorCheckStation station) {
    return Integer.compare(super.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
