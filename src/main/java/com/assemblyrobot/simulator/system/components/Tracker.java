package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Contains tracking data of a {@link Material}.Always has the same Id as the corresponding {@link
 * Material}.
 */

@RequiredArgsConstructor
public class Tracker {

  @Getter
  private final long trackerId;
  @Getter
  private final ArrayList<MaterialMetricsCollector> stationMetrics = new ArrayList<>();

  /** @param metrics Metrics that are added to the {@link Tracker#stationMetrics} ArrayList. */
  public void addMetrics(@NonNull MaterialMetricsCollector metrics) {
    stationMetrics.add(metrics);
  }
}
