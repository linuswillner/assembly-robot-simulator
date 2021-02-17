package com.assemblyrobot.simulator.core.generators;

import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import eduni.distributions.Normal;
import com.assemblyrobot.shared.config.Config;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Arrival event interval generator. Generates arbitrary intervals for {@link
 * com.assemblyrobot.simulator.core.events.EventType#ARRIVAL} events (measured in seconds) based on
 * a {@link Normal} distribution, with defaults specified by the system and alterable by the user.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrivalEventIntervalGenerator implements Generator {
  @Getter
  private static final ArrivalEventIntervalGenerator instance = new ArrivalEventIntervalGenerator();

  private final NormalDistributionConfig config = Config.getConfig().getArrivalIntervalParams();
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
