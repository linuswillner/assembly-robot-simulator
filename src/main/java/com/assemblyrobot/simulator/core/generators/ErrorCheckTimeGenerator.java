package com.assemblyrobot.simulator.core.generators;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import eduni.distributions.Normal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error check time generator. Generates arbitrary durations for error checks (measured in seconds)
 * during the ErrorCheck stage based on a {@link Normal} distribution, with defaults specified by the
 * system and alterable by the user.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCheckTimeGenerator implements Generator {
  @Getter private static final ErrorCheckTimeGenerator instance = new ErrorCheckTimeGenerator();

  private final NormalDistributionConfig config = Config.getConfig().getErrorCheckTimeParams();
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
