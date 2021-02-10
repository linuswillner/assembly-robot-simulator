package com.assemblyrobot.simulator.core.metrics;

import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;

public class MetricsCollector {
  @Getter private final HashMap<String, Double> metrics = new HashMap<>();
  @Getter @NonNull private final MetricsCollectorType type;

  public MetricsCollector(@NonNull String collectingClassName, @NonNull String typeClassName) {
    this.type = MetricsCollectorType.getByClass(typeClassName);
    CentralMetricsCollector.getInstance().registerMetricsCollector(collectingClassName, this);
  }

  public double getMetric(@NonNull String metricName) {
    return metrics.get(metricName);
  }

  public double getMetricWithDefault(@NonNull String metricName, double defaultValue) {
    return metrics.getOrDefault(metricName, defaultValue);
  }

  public void putMetric(@NonNull String metricName, double measurement) {
    metrics.put(metricName, measurement);
  }
}
