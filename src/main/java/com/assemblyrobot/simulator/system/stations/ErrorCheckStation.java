package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.ErrorCheckTimeGenerator;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import eduni.distributions.Normal;
import lombok.NonNull;

/**
 * Error checking duration generator. Generates arbitrary durations for error checking (measured in
 * seconds) during the Assembly stage based on a {@link Normal} distribution, with defaults
 * specified by the system and alterable by the user.
 */
public class ErrorCheckStation extends Station implements Comparable<ErrorCheckStation> {
  public ErrorCheckStation(@NonNull ErrorCheckStage stage) {
    super(stage.getStageController());
  }

  @Override
  protected long getProcessingTime() {
    return ErrorCheckTimeGenerator.getInstance().nextLong();
  }

  // Handles ordering the PriorityQueue inside AssemblyStage
  @Override
  public int compareTo(@NonNull ErrorCheckStation station) {
    return Integer.compare(super.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
