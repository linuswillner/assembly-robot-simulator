package com.assemblyrobot.simulator.core.clock;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Centralised, monotonic simulator clock with support for attaching listeners to monitor clock
 * advancements.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Clock {
  @Getter private static final Clock instance = new Clock();
  @Getter private long currentTick = 0;
  private final ArrayList<TickAdvanceListener> listeners = new ArrayList<>();

  // Clock logic

  /** Move the clock forward by one tick. */
  public void nextTick() {
    currentTick++;
    onTickAdvance(1);
  }

  /**
   * Move the clock forward by a set number of ticks.
   *
   * @param amount Amount to move the clock forward by.
   * @throws IllegalArgumentException If the provided tick advancement is less than 0.
   */
  public void advanceTick(long amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Tick advancements must be > 0.");
    }

    currentTick += amount;
    onTickAdvance(amount);
  }

  /** Reset the clock tick to 0. */
  public void reset() {
    currentTick = 0;
    onTickReset();
  }

  // Tick listener logic

  /**
   * Register a tick advancement listener.
   *
   * @param listener {@link TickAdvanceListener}
   */
  public void registerTickAdvanceListener(TickAdvanceListener listener) {
    listeners.add(listener);
  }

  /**
   * Internal callback that is called whenever the clock moves forward. Propagates calling of the
   * {@link TickAdvanceListener#onTickAdvance(long)} method on each listener.
   *
   * @param ticksAdvanced Amount of ticks that the clock was moved forward by
   */
  private void onTickAdvance(long ticksAdvanced) {
    listeners.forEach(listener -> listener.onTickAdvance(ticksAdvanced));
  }

  /**
   * Internal callback that is called whenever the clock is reset. Propagates calling of the {@link
   * TickAdvanceListener#onTickReset()} method on each listener.
   */
  private void onTickReset() {
    listeners.forEach(TickAdvanceListener::onTickReset);
  }
}
