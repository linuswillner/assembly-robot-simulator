package com.assemblyrobot.simulator.core.clock;

public abstract class TickAdvanceListener {
  protected TickAdvanceListener(Object o) {
    // This constructor does nothing, it's just here to ensure that all TickAdvanceListeners define
    // a constructor (which is where they attach themselves as tick advance listeners)
  }

  protected abstract void onTickAdvance(long ticksAdvanced);

  protected abstract void onTickReset();
}
