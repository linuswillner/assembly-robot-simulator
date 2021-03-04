package com.assemblyrobot.simulator.system.metricscollectors;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class MaterialMetricsCollector {
  @Getter private final StageID stageId;
  @Getter private final String stationId;
  @Getter @Setter private long queueStartTime;
  @Getter @Setter private long queueEndTime;
  @Getter @Setter private long processingStartTime;
  @Getter @Setter private long processingEndTime;

  private final MetricsCollector metricsCollector;

  @RequiredArgsConstructor
  private enum Metrics {
    QUEUE_START_TIME("material_queue_start_time"),
    QUEUE_END_TIME("material_queue_end_time"),
    QUEUE_DURATION("material_queue_duration"),
    PROCESSING_START_TIME("material_processing_start_time"),
    PROCESSING_END_TIME("material_processing_end_time"),
    PROCESSING_DURATION("material_processing_duration"),
    PASSTHROUGH_TIME("material_passthrough_time");

    @Getter private final String metricName;
  }

  public MaterialMetricsCollector(StageID stageId, String stationId, long materialId) {
    this.stageId = stageId;
    this.stationId = stationId;
    metricsCollector =
        new MetricsCollector(
            "Material-%d [%s]".formatted(materialId, stationId),
            getClass().getName());
  }

  public long getQueueDuration() {
    return queueEndTime - queueStartTime;
  }

  public long getProcessingDuration() {
    return processingEndTime - processingStartTime;
  }

  public long getTotalPassthroughTime() {
    return processingEndTime - queueStartTime;
  }

  public void updateMetrics() {
    Arrays.stream(Metrics.values())
        .forEach(
            metric -> {
              switch (metric) {
                case QUEUE_START_TIME -> putMetric(Metrics.QUEUE_START_TIME, queueStartTime);
                case QUEUE_END_TIME -> putMetric(Metrics.QUEUE_END_TIME, queueEndTime);
                case QUEUE_DURATION -> putMetric(Metrics.QUEUE_DURATION, getQueueDuration());
                case PROCESSING_START_TIME -> putMetric(Metrics.PROCESSING_START_TIME, processingStartTime);
                case PROCESSING_END_TIME -> putMetric(Metrics.PROCESSING_END_TIME, processingEndTime);
                case PROCESSING_DURATION -> putMetric(Metrics.PROCESSING_DURATION, getProcessingDuration());
                case PASSTHROUGH_TIME -> putMetric(Metrics.PASSTHROUGH_TIME, getTotalPassthroughTime());
              }
            });
  }

  private void putMetric(Metrics metric, double measurement) {
    metricsCollector.putMetric(metric.getMetricName(), measurement);
  }
}
