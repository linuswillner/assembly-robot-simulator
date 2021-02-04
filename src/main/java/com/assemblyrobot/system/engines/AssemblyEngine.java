package com.assemblyrobot.system.engines;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.events.ArrivalEventGenerator;
import com.assemblyrobot.system.materials.ComponentPack;
import com.assemblyrobot.system.points.AssemblyStation;
import java.util.Arrays;

public class AssemblyEngine extends Engine {
  private final ArrivalEventGenerator arrivalEventGenerator =
      new ArrivalEventGenerator(super.getEventQueue());

  private final AssemblyStation[] assemblyPoints = {
      new AssemblyStation(super.getEventQueue())
  };

  @Override
  protected void init() {
    // Register points
    super.getStations().addAll(Arrays.asList(assemblyPoints));

    // Kickstart arrival event generator
    arrivalEventGenerator.feedNext();
  }

  @Override
  protected void onArrival() {
    arrivalEventGenerator.feedNext();
    // TODO: Temp, automatically hand out the best one
    super.getStations().get(0).addToQueue(new ComponentPack());
  }

  @Override
  protected void onDeparture() {}
}
