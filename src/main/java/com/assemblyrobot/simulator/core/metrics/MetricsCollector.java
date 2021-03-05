package com.assemblyrobot.simulator.core.metrics;

import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

/**
 * Generic metrics collector. Can be attached to any class to store metrics about various facets of
 * said class.
 */
public class MetricsCollector {
  @Getter private final String hostName;
  @Getter private final HashMap<String, Double> metrics = new HashMap<>();
  @Getter @NonNull private final MetricsCollectorType type;

  public MetricsCollector(@NonNull String hostName, @NonNull String typeClassName) {
    this.hostName = hostName;
    this.type = MetricsCollectorType.getByClass(typeClassName);
    CentralMetricsCollector.getInstance().registerMetricsCollector(hostName, this);
  }

  /**
   * Gets a metric by name.
   *
   * @param metricName Metric name to get.
   * @return {@link Double}
   * @throws NullPointerException If no metric by this name exists.
   */
  public double getMetric(@NonNull String metricName) throws NullPointerException {
    if (!metrics.containsKey(metricName)) {
      throw new NullPointerException("No metric with name %s found.".formatted(metricName));
    }

    return metrics.get(metricName);
  }

  /**
   * Gets a metric by name, specifying a default value to use when a metric by this name does not
   * exist.
   *
   * @param metricName Metric name to get.
   * @param defaultValue Default value to use in case no metric by this name exists.
   * @return {@link Double}
   */
  public double getMetric(@NonNull String metricName, double defaultValue) {
    return metrics.getOrDefault(metricName, defaultValue);
  }

  /**
   * Sets or updates a metric.
   *
   * @param metricName Metric name to set/update.
   * @param measurement Metric value to set/update.
   */
  public void putMetric(@NonNull String metricName, double measurement) {
    metrics.put(metricName, measurement);
  }

  /**
   * Increments a metric by 1.
   *
   * @param metricName Metric name to increment.
   */
  public void incrementMetric(@NonNull String metricName) {
    val current = getMetric(metricName, 0);
    metrics.put(metricName, current + 1);
  }

  /**
   * Increments a metric by wanted amount.
   *
   * @param metricName Metric name to increment.
   * @param measurement Amount to increment.
   */
  public void incrementMetric(@NonNull String metricName, double measurement) {
    val current = getMetric(metricName, 0);
    metrics.put(metricName, current + measurement);
  }
}
