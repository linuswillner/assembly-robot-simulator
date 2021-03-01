package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.FixStage;
import lombok.NonNull;

public class FixStation extends Station implements Comparable<FixStation> {
  private final ErrorType type;

  public FixStation(@NonNull FixStage stage, ErrorType type) {
    super(stage.getStageController());
    this.type = type;
  }

  @Override
  protected long getProcessingTime() {
    return type.getFixTime();
  }

  @Override
  public int compareTo(@NonNull FixStation station) {
    return Integer.compare(super.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
