package com.assemblyrobot.simulator.core.generators;

/**
 * Generic generator interface. Used to enforce a consistent API for all random number generators in
 * the simulator.
 */
public interface Generator {
  /**
   * Gets the next random value as an {@link Integer}.
   *
   * @return {@link Integer}
   */
  int nextInt();

  /**
   * Gets the next random value as a {@link Long}.
   *
   * @return {@link Long}
   */
  long nextLong();

  /**
   * Gets the next random value as a {@link Double}.
   *
   * @return {@link Double}
   */
  double nextDouble();
}
