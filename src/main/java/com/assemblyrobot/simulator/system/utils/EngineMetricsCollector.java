package com.assemblyrobot.simulator.system.utils;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import lombok.NonNull;
import lombok.val;

public class EngineMetricsCollector extends TickAdvanceListener {
  private final MetricsCollector metricsCollector;
  private static final String TOTAL_SIMULATION_TIME_METRIC_NAME = "total_simulation_time";

  // Cannot use RequiredArgsConstructor and a class property here because that will result in a
  // "variable might not have been initialized" error
  public EngineMetricsCollector(@NonNull Engine engine) {
    metricsCollector = new MetricsCollector(getClass().getName(), engine.getClass().getSuperclass().getName());
  }

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    val currentTime = metricsCollector.getMetric(TOTAL_SIMULATION_TIME_METRIC_NAME, 0);
    metricsCollector.putMetric(TOTAL_SIMULATION_TIME_METRIC_NAME, currentTime + ticksAdvanced);
  }

  @Override
  protected void onTickReset() {
    metricsCollector.putMetric(TOTAL_SIMULATION_TIME_METRIC_NAME, 0);
  }
}
