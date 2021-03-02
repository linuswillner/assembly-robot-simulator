package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import lombok.Getter;
import lombok.Setter;

public class MaterialStationData {
  @Getter private final StageID stageId;
  @Getter private final String stationId;
  @Getter @Setter private long queueStartTime;
  @Getter @Setter private long queueEndTime;
  @Getter @Setter private long processingStartTime;
  @Getter @Setter private long processingEndTime;

  private final MetricsCollector metricsCollector;
  private static final String MATERIAL_PASSTHROUGH_TIME_METRIC_NAME = "material_passthrough_time";

  public MaterialStationData(StageID stageId, String stationId) {
    this.stageId = stageId;
    this.stationId = stationId;
    metricsCollector =
        new MetricsCollector(
            "%s-%s".formatted(getClass().getSimpleName(), stationId), getClass().getName());
  }

  public long getQueueDuration() {
    return queueEndTime - queueStartTime;
  }

  public long getProcessingDuration() {
    return processingEndTime - processingStartTime;
  }

  public long getTotalProcessingTime() {
    return processingEndTime - queueStartTime;
  }
}
