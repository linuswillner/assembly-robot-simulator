package com.assemblyrobot.simulator.system;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.system.utils.ArrivalEventPropagator;

public class SimulatorEngine extends Engine {
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
  protected void onTransfer() {
    super.getStageController().transferAll();
  }

  @Override
  protected void onDeparture() {}
}
