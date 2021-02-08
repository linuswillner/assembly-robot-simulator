package com.assemblyrobot.simulator.core.clock;

public abstract class TickAdvanceListener {
  protected TickAdvanceListener() {
    Clock.getInstance().registerTickAdvanceListener(this);
  }

  protected abstract void onTickAdvance(long ticksAdvanced);

  protected abstract void onTickReset();
}
