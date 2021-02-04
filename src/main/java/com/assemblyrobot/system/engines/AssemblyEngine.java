package com.assemblyrobot.system.engines;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.events.ArrivalEventGenerator;
import com.assemblyrobot.system.materials.ComponentPack;
import com.assemblyrobot.system.points.AssemblyPoint;
import java.util.Arrays;

public class AssemblyEngine extends Engine {
  private final ArrivalEventGenerator arrivalEventGenerator =
      new ArrivalEventGenerator(super.getEventQueue());

  private final AssemblyPoint[] assemblyPoints = {
      new AssemblyPoint(super.getEventQueue())
  };

  @Override
  protected void init() {
    // Register points
    super.getPoints().addAll(Arrays.asList(assemblyPoints));

    // Kickstart arrival event generator
    arrivalEventGenerator.feedNext();
  }

  @Override
  protected void onArrival() {
    arrivalEventGenerator.feedNext();
    // TODO: Temp, automatically hand out the best one
    super.getPoints().get(0).addToQueue(new ComponentPack());
  }

  @Override
  protected void onDeparture() {}
}
