package com.assemblyrobot.simulator.system.metricscollectors;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * Collects metrics from the {@link Engine}. This exists as a subclass to make it a {@link
 * TickAdvanceListener}, thus making collecting simulation time easier.
 */
public class EngineMetricsCollector extends TickAdvanceListener {
  private final MetricsCollector metricsCollector;

  @RequiredArgsConstructor
  public enum Metrics {
    TOTAL_SIMULATION_TIME("total_simulation_time");

    @Getter private final String metricName;
  }

  // Cannot use RequiredArgsConstructor and a class property here because that will result in a
  // "variable might not have been initialized" error
  public EngineMetricsCollector(@NonNull Engine engine) {
    metricsCollector =
        new MetricsCollector(
            getClass().getSimpleName(), engine.getClass().getSuperclass().getName());
  }

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    val currentTime = metricsCollector.getMetric(Metrics.TOTAL_SIMULATION_TIME.getMetricName(), 0);
    metricsCollector.putMetric(
        Metrics.TOTAL_SIMULATION_TIME.getMetricName(), currentTime + ticksAdvanced);
  }

  @Override
  protected void onTickReset() {
    metricsCollector.putMetric(Metrics.TOTAL_SIMULATION_TIME.getMetricName(), 0);
  }
}
