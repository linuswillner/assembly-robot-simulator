package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Contains tracking data of a {@link Material}. Always has the same ID as the {@link Material} it
 * corresponds to.
 */
@RequiredArgsConstructor
public class Tracker {
  @Getter private final long trackerId;
  @Getter private final ArrayList<MaterialMetricsCollector> stationMetrics = new ArrayList<>();

  /**
   * Logs metrics from processing that happened in a given {@link Station}, as logged by a {@link
   * MaterialMetricsCollector}.
   *
   * @param metrics Metrics logged in the {@link Station}.
   */
  public void addMetrics(@NonNull MaterialMetricsCollector metrics) {
    stationMetrics.add(metrics);
  }
}
