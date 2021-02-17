package com.assemblyrobot.simulator.core.generators;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import eduni.distributions.Normal;
import lombok.Getter;

// TODO: When there is an AssemblyStage, link it here
/**
 * Assembly duration generator. Generates arbitrary durations for assembly (measured in seconds)
 * during the Assembly stage based on a {@link Normal} distribution, with defaults specified by the
 * system and alterable by the user.
 */
public class AssemblyTimeGenerator implements Generator {
  @Getter private static final AssemblyTimeGenerator instance = new AssemblyTimeGenerator();

  private final NormalDistributionConfig config = Config.getConfig().getAssemblyTimeParams();
  private final Normal normalGenerator = new Normal(config.getMean(), config.getVariance());

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
