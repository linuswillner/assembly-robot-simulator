package com.assemblyrobot.simulator.system;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.TransferEvent;
import com.assemblyrobot.simulator.system.utils.ArrivalEventPropagator;
import lombok.NonNull;

public class SimulatorEngine extends Engine {

  private final ArrivalEventPropagator arrivalEventPropagator =
      new ArrivalEventPropagator(super.getEventQueue());

  @Override
  protected void init() {
    // Kickstart arrival event generator
    arrivalEventPropagator.feedNext();
  }

  @Override
  protected void onArrival(@NonNull Event event) {
    arrivalEventPropagator.feedNext();
    super.getStageController().registerIncomingMaterial();
  }

  @Override
  protected void onTransfer(@NonNull TransferEvent event) {

    super.getStageController().transferAll();
  }

  @Override
  protected void onDeparture(Event event) {
  }
}
