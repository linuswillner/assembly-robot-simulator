package com.assemblyrobot.simulator.system.metricscollectors;

import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.components.Station;
import lombok.NonNull;

public class StationMetricsCollector {
  private final MetricsCollector metricsCollector;

  public StationMetricsCollector(@NonNull Station station){
    metricsCollector = new MetricsCollector(getClass().getName(), station.getClass().getSuperclass().getName());
  }
}
