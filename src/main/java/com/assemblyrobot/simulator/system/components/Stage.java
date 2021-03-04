package com.assemblyrobot.simulator.system.components;

import lombok.NonNull;

/**
 * Abstract super class for all stage classes. Contains methods to control a material's flow to in a
 * stage.
 */
public abstract class Stage {

  /** Creates the wanted amount of stations. */
  protected abstract void createStations();

  /**
   * Checks which station is free and commands it to add the material to its queue.
   *
   * @param material material to be added
   */
  public abstract void addToStationQueue(@NonNull Material material);
}
