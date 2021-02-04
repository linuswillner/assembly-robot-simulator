package com.assemblyrobot.system.controllers;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import lombok.Getter;

public class EngineController extends TickAdvanceListener {

  public EngineController() {
    super(null);
    Clock.getInstance().registerTickAdvanceListener(this);
  }

  @Getter private long totalSimulationTime = 0;

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    totalSimulationTime += ticksAdvanced;
  }

  @Override
  protected void onTickReset() {
    totalSimulationTime = 0;
  }
}
