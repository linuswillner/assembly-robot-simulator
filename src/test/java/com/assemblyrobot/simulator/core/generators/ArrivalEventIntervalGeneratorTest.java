package com.assemblyrobot.simulator.core.generators;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.Test;

class ArrivalEventIntervalGeneratorTest {
  private final ArrivalEventIntervalGenerator generator =
      ArrivalEventIntervalGenerator.getInstance();

  @Test
  void nextInt() {
    val result = generator.nextInt();
    assertTrue(result >= 15 && result <= 25);
  }

  @Test
  void nextLong() {
    val result = generator.nextLong();
    assertTrue(result >= 15L && result <= 25L);
  }

  @Test
  void nextDouble() {
    val result = generator.nextDouble();
    assertTrue(result >= 15.0 && result <= 25.0);
  }
}
