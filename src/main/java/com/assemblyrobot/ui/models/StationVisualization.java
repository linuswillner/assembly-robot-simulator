package com.assemblyrobot.ui.models;

import lombok.Getter;

/**
 * Visualisation transformer class for {@link
 * com.assemblyrobot.simulator.system.components.Station}s.
 */
public class StationVisualization {
  @Getter private String name;
  @Getter private String status;
  @Getter private final int queueLength;

  public StationVisualization(String name, boolean isBusy, int queueLength) {
    this.name = name;
    setStatus(isBusy);
    this.queueLength = queueLength;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStatus(boolean isBusy) {
    if (isBusy) {
      status = "Busy";
    } else {
      status = "Idle";
    }
  }
}
