package com.assemblyrobot.simulator.system.engines;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.system.generators.ArrivalEventPropagator;

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
    super.getStageController().registerIncomingMaterial();
  }

  @Override
  protected void onDeparture() {}
}
