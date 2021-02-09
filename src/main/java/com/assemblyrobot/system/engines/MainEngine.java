package com.assemblyrobot.system.engines;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.system.generators.ArrivalEventPropagator;
import com.assemblyrobot.system.stations.AssemblyStation;
import java.util.Arrays;

public class MainEngine extends Engine {
  private final ArrivalEventPropagator arrivalEventPropagator =
      new ArrivalEventPropagator(super.getEventQueue());

  @Override
  protected void init() {
    // Kickstart arrival event generator
    arrivalEventPropagator.feedNext();
  }

  @Override
  protected void onArrival() {
    arrivalEventPropagator.feedNext();
  }

  @Override
  protected void onDeparture() {}
}
