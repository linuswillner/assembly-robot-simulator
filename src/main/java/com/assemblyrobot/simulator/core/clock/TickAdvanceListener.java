package com.assemblyrobot.simulator.core.clock;

/**
 * Generic tick advancement listener. Any class that extends this class will automatically be
 * registered as a tick advancement listener in the {@link Clock}.
 */
public abstract class TickAdvanceListener {
  protected TickAdvanceListener() {
    Clock.getInstance().registerTickAdvanceListener(this);
  }

  /**
   * Callback method for when the clock moves forward.
   *
   * @param ticksAdvanced The amount of ticks that the clock moved forward by.
   */
  protected abstract void onTickAdvance(long ticksAdvanced);

  /** Callback method for when the clock tick was reset to 0. */
  protected abstract void onTickReset();
}
