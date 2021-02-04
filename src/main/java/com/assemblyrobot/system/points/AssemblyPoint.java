package com.assemblyrobot.system.points;

import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.system.core.Point;

public class AssemblyPoint extends Point {
  public AssemblyPoint(EventQueue eventQueue) {
    super(eventQueue);
  }

  @Override
  protected long getProcessingTime() {
    return 5; // TODO: Deconstantify
  }
}
