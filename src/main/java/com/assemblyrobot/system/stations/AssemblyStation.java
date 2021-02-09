package com.assemblyrobot.system.stations;

import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.system.core.Station;

public class AssemblyStation extends Station {

  public AssemblyStation() {
    super();
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }
}
