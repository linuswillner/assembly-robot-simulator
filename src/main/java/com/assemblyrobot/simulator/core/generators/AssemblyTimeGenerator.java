package com.assemblyrobot.simulator.core.generators;

import eduni.distributions.Normal;
import lombok.Getter;

public class AssemblyTimeGenerator implements Generator {
  @Getter private static final AssemblyTimeGenerator instance = new AssemblyTimeGenerator();

  // TODO: Load from config, deconstantify parameters
  private final Normal normalGenerator = new Normal(10, 2);

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
