package com.assemblyrobot.simulator.system.metricscollectors;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class EngineMetricsCollector extends TickAdvanceListener {
  private final Engine engine;
  private final MetricsCollector metricsCollector =
      new MetricsCollector(getClass().getName(), engine.getClass().getName());
  private static final String TOTAL_SIMULATION_TIME_METRIC_NAME = "total_simulation_time";

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
