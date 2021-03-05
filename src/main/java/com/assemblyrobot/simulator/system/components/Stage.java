package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.ErrorType;
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
   * @param material Material to be added.
   * @param errorType Optional error type, if calling this on a {@link
   *     com.assemblyrobot.simulator.system.stages.FixStage}.
   */
  public abstract void addToStationQueue(@NonNull Material material, ErrorType errorType);
}
