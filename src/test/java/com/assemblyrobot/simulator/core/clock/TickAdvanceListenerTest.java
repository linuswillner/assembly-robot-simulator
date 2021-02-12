package com.assemblyrobot.simulator.core.clock;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class TickAdvanceListenerTest {
  private final Clock clock = Clock.getInstance();
  private boolean onTickAdvanceCalled = false;
  private boolean onTickResetCalled = false;
  private TestListener testListener;

  private class TestListener extends TickAdvanceListener {
    @Override
    protected void onTickAdvance(long ticksAdvanced) {
      onTickAdvanceCalled = true;
    }

    @Override
    protected void onTickReset() {
      onTickResetCalled = true;
    }
  }

  @BeforeAll
  void beforeAll() {
    clock.reset();
    testListener = new TestListener();
  }

  @Test
  @DisplayName("onTickAdvance(): Is called when clock moves forward")
  void onTickAdvance() {
    clock.nextTick();
    assertTrue(onTickAdvanceCalled);
  }

  @Test
  @DisplayName("onTickReset(): Is called when clock tick is reset")
  void onTickReset() {
    clock.reset();
    assertTrue(onTickResetCalled);
  }
}
