package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.simulator.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Station;

public class AssemblyStation extends Station {

  public AssemblyStation() {
    super();
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }

  @Override
  public void onChildQueueDepart(Material material, MaterialStationData data) {

  }
}
