package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class Station extends TickAdvanceListener {
  private static int nextFreeId = 1;

  @Getter(AccessLevel.PROTECTED)
  private final MetricsCollector metricsCollector =
      new MetricsCollector(
          "%s-%d".formatted(getClass().getSimpleName(), nextFreeId), getClass().getName());

  public Station() {
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }

  public abstract boolean isBusy();

  protected abstract boolean canPull();

  public abstract void addToStationQueue(Material material, MaterialStationData stationData);

  protected abstract Material pullFromStationQueue();

  public abstract void poll();

  protected abstract long getProcessingTime();
}
