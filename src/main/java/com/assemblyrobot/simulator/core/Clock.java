package com.assemblyrobot.simulator.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Clock {
  @Getter private static final Clock instance = new Clock();
  @Getter private long currentTick = 0;

  public void nextTick() {
    currentTick++;
  }

  public void advanceTick(int amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Tick advancements must be > 0.");
    }

    currentTick += amount;
  }

  public void advanceTick(long amount) throws IllegalArgumentException {
    if (amount <= 0) {
      throw new IllegalArgumentException("Tick advancements must be > 0.");
    }

    currentTick += amount;
  }

  public void reset() {
    currentTick = 0;
  }
}
