package com.assemblyrobot.simulator.core.clock;

import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Clock {
  @Getter private static final Clock instance = new Clock();
  @Getter private long currentTick = 0;
  private final ArrayList<TickAdvanceListener> listeners = new ArrayList<>();

  // Clock logic

  public void nextTick() {
    currentTick++;
    onTickAdvance(1);
  }

  public void advanceTick(int amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Tick advancements must be > 0.");
    }

    currentTick += amount;
    onTickAdvance(amount);
  }

  public void advanceTick(long amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Tick advancements must be > 0.");
    }

    currentTick += amount;
    onTickAdvance(amount);
  }

  public void reset() {
    currentTick = 0;
    onTickReset();
  }

  // Tick listener logic

  public void registerTickAdvanceListener(TickAdvanceListener listener) {
    listeners.add(listener);
  }

  private void onTickAdvance(long ticksAdvanced) {
    listeners.forEach(listener -> listener.onTickAdvance(ticksAdvanced));
  }

  private void onTickReset() {
    listeners.forEach(TickAdvanceListener::onTickReset);
  }
}
