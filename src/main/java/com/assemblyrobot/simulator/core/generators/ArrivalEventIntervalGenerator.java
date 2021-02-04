package com.assemblyrobot.simulator.core.generators;

import eduni.distributions.Normal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrivalEventIntervalGenerator implements Generator {
  @Getter
  private static final ArrivalEventIntervalGenerator instance = new ArrivalEventIntervalGenerator();

  // TODO: Load from config, deconstantify
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
