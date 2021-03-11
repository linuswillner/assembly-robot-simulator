package com.assemblyrobot.simulator.system;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.TransferEvent;
import com.assemblyrobot.simulator.system.utils.ArrivalEventPropagator;
import lombok.NonNull;
import com.assemblyrobot.ui.controllers.OverviewController;

public class SimulatorEngine extends Engine {

  private final ArrivalEventPropagator arrivalEventPropagator =
      new ArrivalEventPropagator(super.getEventQueue());
  private final OverviewController overviewController;

  public SimulatorEngine(OverviewController overviewController){
    this.overviewController = overviewController;
  }

  @Override
  protected void init() {
    // Kickstart arrival event generator
    arrivalEventPropagator.feedNext();
  }

  @Override
  protected void onArrival(@NonNull Event event) {
    arrivalEventPropagator.feedNext();
    super.getStageController().registerIncomingMaterial();
    overviewController.onArrival();
  }

  @Override
  protected void onTransfer(TransferEvent event) {
    // Tells UI how to animate the transfers of a given material
    switch (event.getDestination()){
      case ERROR_CHECK -> overviewController.onTransfer("error_check");

      case FIX -> {
        if (event.getError() != null) {
          switch (event.getError()) {
            case BOLTING ->overviewController.onTransfer("bolting");
            case FITTING ->overviewController.onTransfer("fitting");
            case WELDING -> overviewController.onTransfer("welding");
            case RIVETING ->overviewController.onTransfer("riveting");
            case RETURNING ->overviewController.onTransfer("positioning");
          }
        }
      }

      case DEPART -> {
        switch (event.getCurrent()){
          case FIX -> {
            if (event.getError() != null) {
              switch (event.getError()) {
                case BOLTING -> overviewController.onTransfer("from_bolting");
                case FITTING -> overviewController.onTransfer("from_fitting");
                case WELDING -> overviewController.onTransfer("from_welding");
                case RIVETING -> overviewController.onTransfer("from_riveting");
                case RETURNING -> overviewController.onTransfer("from_positioning");
              }
            }
          }
          case ERROR_CHECK -> overviewController.onTransfer("departure");
        }
      }
    }

    super.getStageController().transferAll();
  }

  @Override
  protected void onDeparture(Event event) {
  }
}
