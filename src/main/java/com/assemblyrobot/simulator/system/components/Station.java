package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;

public abstract class Station extends TickAdvanceListener {

  public abstract boolean isBusy();

  protected abstract boolean canPull();

  public abstract void addToStationQueue(Material material, MaterialStationData stationData);

  protected abstract Material pullFromStationQueue();

  public abstract void poll();

  protected abstract long getProcessingTime();
}
