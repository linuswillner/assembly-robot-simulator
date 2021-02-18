package com.assemblyrobot.simulator.system.components;

import lombok.NonNull;

public abstract class Stage {

  protected abstract void createStations(@NonNull int stationAmount);

  public abstract void addToStationQueue(@NonNull Material material);
}
