package com.assemblyrobot.simulator.core.metrics;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CentralMetricsCollector {
  @Getter private static final CentralMetricsCollector instance = new CentralMetricsCollector();

  @Getter
  private static final HashMap<MetricsCollector.Type, MetricsCollector> collectors =
      new HashMap<>();

  public void registerMetricsCollector(MetricsCollector collector) {
    val collectorClassName = collector.getClass().getSuperclass().getName();
    collectors.put(MetricsCollector.Type.getByClass(collectorClassName), collector);
  }
}
