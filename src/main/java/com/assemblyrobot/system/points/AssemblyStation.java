package com.assemblyrobot.system.points;

import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.system.core.Station;

public class AssemblyStation extends Station {
  public AssemblyStation(EventQueue eventQueue) {
    super(eventQueue);
  }

  @Override
  protected long getProcessingTime() {
    return 5; // TODO: Deconstantify
  }
}
