package com.assemblyrobot.simulator.core.generators;

import eduni.distributions.Normal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Arrival event interval generator. Generates arbitrary intervals for {@link
 * com.assemblyrobot.simulator.core.events.EventType}.ARRIVAL events (measured in seconds) based on
 * a {@link Normal} distribution, with defaults specified by the system and alterable by the user.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrivalEventIntervalGenerator implements Generator {
  @Getter
  private static final ArrivalEventIntervalGenerator instance = new ArrivalEventIntervalGenerator();

  // TODO: Load from config, deconstantify parameters
  private final Normal normalGenerator = new Normal(20, 5);

  @Override
  public int nextInt() {
    return (int) Math.round(normalGenerator.sample());
  }

  @Override
  public long nextLong() {
    return Math.round(normalGenerator.sample());
  }

  @Override
  public double nextDouble() {
    return normalGenerator.sample();
  }
}
