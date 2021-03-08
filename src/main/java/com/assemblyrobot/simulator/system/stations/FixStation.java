package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.stages.FixStage;
import java.util.HashMap;
import lombok.NonNull;

public class FixStation extends Station implements Comparable<FixStation> {
  private final ErrorType type;
  private static final HashMap<ErrorType, Long> stationIds = new HashMap<>();

  public FixStation(@NonNull FixStage stage, ErrorType type) {
    super(
        stage.getStageController(),
        "FixStation-%s-%d".formatted(type, stationIds.getOrDefault(type, 1L)));
    this.type = type;

    // Having to do increment separately here because super() has to be the first call in the
    // constructor
    var currentFreeId = stationIds.getOrDefault(type, 1L);
    stationIds.put(type, ++currentFreeId);
  }

  public static void resetId() {
    stationIds
        .keySet()
        .forEach(
            errorType -> {
              stationIds.put(errorType, 1L);
            });
  }

  @Override
  protected long getProcessingTime() {
    return type.getFixTime();
  }

  @Override
  public int compareTo(@NonNull FixStation station) {
    return Integer.compare(this.getMaterialQueue().size(), station.getMaterialQueue().size());
  }
}
