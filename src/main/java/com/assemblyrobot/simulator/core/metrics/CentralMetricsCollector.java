package com.assemblyrobot.simulator.core.metrics;

import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CentralMetricsCollector {
  @Getter private static final CentralMetricsCollector instance = new CentralMetricsCollector();

  @Getter private static final HashMap<String, MetricsCollector> collectors = new HashMap<>();

  public void registerMetricsCollector(
      @NonNull String hostClassName, @NonNull MetricsCollector collector) {
    collectors.put(hostClassName, collector);
  }
}
