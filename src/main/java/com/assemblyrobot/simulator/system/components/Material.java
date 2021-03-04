package com.assemblyrobot.simulator.system.components;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Material implements Comparable<Material> {
  private static int nextFreeId = 1;

  @Getter private final long id;
  @Getter @Setter private long queueStartTime = 0;
  @Getter @Setter private long queueEndTime = 0;
  @Getter @Setter private long processingStartTime = 0;
  @Getter @Setter private long processingEndTime = 0;

  public Material() {
    id = nextFreeId;
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }


  public void reset() {
    queueStartTime = 0;
    queueEndTime = 0;
    processingStartTime = 0;
    processingEndTime = 0;
  }


  /**
   * calculates the amount of time the {@link Material} spent in the system
   * @return returns the difference of {@link Material#processingEndTime} and {@link Material#processingStartTime}
   */
  public long getTotalPassthroughTime() {
    return processingEndTime - queueStartTime;
  }


  public int compareTo(Material material) {
    return Long.compare(this.getQueueStartTime(), material.getQueueStartTime());
  }
}
