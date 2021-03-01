package com.assemblyrobot.simulator.core.generators;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import eduni.distributions.Normal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Assembly duration generator. Generates arbitrary durations for assembly (measured in seconds)
 * during the Assembly stage based on a {@link Normal} distribution, with defaults specified by the
 * system and alterable by the user.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssemblyTimeGenerator implements Generator {
  @Getter private static final AssemblyTimeGenerator instance = new AssemblyTimeGenerator();

  private final NormalDistributionConfig config = Config.getConfig().getAssemblyTimeParams();
  private final Normal timeGenerator = new Normal(config.getMean(), config.getVariance());

  @Override
  public int nextInt() {
    return (int) Math.round(timeGenerator.sample());
  }

  @Override
  public long nextLong() {
    return Math.round(timeGenerator.sample());
  }

  @Override
  public double nextDouble() {
    return timeGenerator.sample();
  }
}
