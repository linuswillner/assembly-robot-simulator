package com.assemblyrobot.simulator.system.components;

import lombok.NonNull;

/**
 * Abstract super class for all stage classes. Contains methods to control a {@link Material}'s flow
 * to in a {@link Stage}.
 */
public abstract class Stage {

  /** Creates the wanted amount of {@link Station}s. */
  protected abstract void createStations();

  /**
   * Checks which {@link Station} is free and commands it to add the {@link Material} to its queue.
   *
   * @param material {@link Material} to be added.
   */
  public abstract void addToStationQueue(@NonNull Material material);
}
