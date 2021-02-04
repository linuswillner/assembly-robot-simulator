package com.assemblyrobot.system.engines;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.system.generators.ArrivalEventPropagator;
import com.assemblyrobot.system.materials.ComponentPack;
import com.assemblyrobot.system.stations.AssemblyStation;
import java.util.Arrays;

public class AssemblyEngine extends Engine {
  private final ArrivalEventPropagator arrivalEventPropagator =
      new ArrivalEventPropagator(super.getEventQueue());

  private final AssemblyStation[] assemblyPoints = {new AssemblyStation(super.getEventQueue())};

  @Override
  protected void init() {
    // Register stations
    super.getStations().addAll(Arrays.asList(assemblyPoints));

    // Kickstart arrival event generator
    arrivalEventPropagator.feedNext();
  }

  @Override
  protected void onArrival() {
    arrivalEventPropagator.feedNext();
    // TODO: Temp, automatically hand out the best one (StageController)
    super.getStations().get(0).addToQueue(new ComponentPack());
  }

  @Override
  protected void onDeparture() {}
}
