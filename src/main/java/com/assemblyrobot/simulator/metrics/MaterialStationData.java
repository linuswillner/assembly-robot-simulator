package com.assemblyrobot.simulator.metrics;

import com.assemblyrobot.simulator.system.stations.StationID;
import lombok.Getter;

public class MaterialStationData {

  @Getter private StationID stationid;
  @Getter private long queueStartTime;
  @Getter private long queueEndTime;
  @Getter private long processingStartTime;
  @Getter private long processingEndTime;

}
