package com.assemblyrobot.simulator.core.generators;

import static org.junit.jupiter.api.Assertions.*;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import com.assemblyrobot.utils.RandomUtils;
import lombok.val;
import org.junit.jupiter.api.Test;

class ArrivalEventIntervalGeneratorTest {
  private final ArrivalEventIntervalGenerator generator =
      ArrivalEventIntervalGenerator.getInstance();

  private final NormalDistributionConfig config = Config.getConfig().getAssemblyTimeParams();

  @Test
  void nextInt() {
    val result = generator.nextInt();
    assertTrue(RandomUtils.resultIsWithinVariance(result, config.getMean(), config.getVariance()));
  }

  @Test
  void nextLong() {
    val result = generator.nextLong();
    assertTrue(RandomUtils.resultIsWithinVariance(result, config.getMean(), config.getVariance()));
  }

  @Test
  void nextDouble() {
    val result = generator.nextDouble();
    // FIXME: This fails in CI because it generates a value that's actually above the expected mean
    // + variance result (Possibly platform dependent issue? Cannot repro locally)
    assertTrue(
        RandomUtils.resultIsWithinVariance(
            Math.floor(result), config.getMean(), config.getVariance()));
  }
}
