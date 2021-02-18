package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import lombok.NonNull;
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

  protected abstract long getProcessingTime();

  protected abstract boolean isBusy();

  protected abstract boolean canPull();

  public abstract void addToStationQueue(
      @NonNull Material material, @NonNull MaterialStationData stationData);

  protected abstract Material pullFromStationQueue();

  protected abstract void poll();
}
