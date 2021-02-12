package com.assemblyrobot.simulator.core.clock;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClockTest {
  private final Clock clock = Clock.getInstance();

  @AfterEach
  void afterEach() {
    clock.reset();
  }

  @Test
  @DisplayName("nextTick(): Advances linearly from 0 to 1")
  void nextTick() {
    clock.nextTick();
    assertEquals(1, clock.getCurrentTick());
  }

  @Test
  @DisplayName("advanceTick(): Advances linearly from 0 to 2")
  void advanceTick() {
    clock.advanceTick(2);
    assertEquals(2, clock.getCurrentTick());
  }

  @Test
  @DisplayName("advanceTickThrows(): Throws an error when a negative tick amount is submitted")
  void advanceTickThrows() {
    assertThrows(IllegalArgumentException.class, () -> clock.advanceTick(-1));
  }

  @Test
  @DisplayName("reset(): Resets clock tick to 0")
  void reset() {
    clock.advanceTick(2);
    clock.reset();
    assertEquals(0, clock.getCurrentTick());
  }
}