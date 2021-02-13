package com.assemblyrobot.simulator.core.generators;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.Test;

class AssemblyTimeGeneratorTest {
  private final AssemblyTimeGenerator generator = AssemblyTimeGenerator.getInstance();

  @Test
  void nextInt() {
    val result = generator.nextInt();
    assertTrue(result >= 8 && result <= 12);
  }

  @Test
  void nextLong() {
    val result = generator.nextLong();
    assertTrue(result >= 8L && result <= 12L);
  }

  @Test
  void nextDouble() {
    val result = generator.nextDouble();
    assertTrue(result >= 8.0 && result <= 12.0);
  }
}
