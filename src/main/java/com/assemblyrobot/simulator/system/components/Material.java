package com.assemblyrobot.simulator.system.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class Material {
  private static int nextFreeId = 1;

  @Getter private final int id;
  @Getter @Setter private long queueStartTime = 0;
  @Getter @Setter private long queueEndTime = 0;
  @Getter @Setter private long processingStartTime = 0;
  @Getter @Setter private long processingEndTime = 0;

  public Material() {
    id = nextFreeId;
    nextFreeId++;
  }

  public long getQueueDuration() {
    return queueEndTime - queueStartTime;
  }

  public long getProcessingDuration() {
    return processingEndTime - processingStartTime;
  }
}
