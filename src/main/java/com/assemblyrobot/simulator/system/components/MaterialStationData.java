package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.system.stages.StageID;
import lombok.Getter;
import lombok.Setter;

public class MaterialStationData {

  @Getter @Setter private StageID stageId;
  @Getter @Setter private long queueStartTime;
  @Getter @Setter private long queueEndTime;
  @Getter @Setter private long processingStartTime;
  @Getter @Setter private long processingEndTime;

}
