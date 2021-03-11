package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.shared.constants.StageID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a product traveling through the production line in a simulated environment.
 */
@ToString
public class Material implements Comparable<Material> {

  private static int nextFreeId = 1;

  @Getter
  private final long id;
  @Getter
  @Setter
  private long queueStartTime = 0;
  @Getter
  @Setter
  private long queueEndTime = 0;
  @Getter
  @Setter
  private long processingStartTime = 0;
  @Getter
  @Setter
  private long processingEndTime = 0;
  @Getter
  @Setter
  private StageID currentStage;
  @Getter
  @Setter
  private ErrorType error;

  public Material() {
    id = nextFreeId;
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }

  /**
   * Resets all class fields to their default values. Used when transferring between {@link Stage}s,
   * since this object is not garbage collected until it has passed through the entire system.
   */
  public void reset() {
    queueStartTime = 0;
    queueEndTime = 0;
    processingStartTime = 0;
    processingEndTime = 0;
  }

  /**
   * Alternatively worded setter for {@link Material#currentStage}. Used for setting the "new"
   * current stage in a more declarative way.
   *
   * @param nextStage The next {@link Stage} this material is headed to.
   */
  public void setNextStage(@NonNull StageID nextStage) {
    this.currentStage = nextStage;
  }

  /**
   * Calculates the amount of time the {@link Material} spent in the system.
   *
   * @return The difference of {@link Material#processingEndTime} and {@link
   * Material#processingStartTime}.
   */
  public long getTotalPassthroughTime() {
    return processingEndTime - queueStartTime;
  }

  public int compareTo(Material material) {
    return Long.compare(this.getQueueStartTime(), material.getQueueStartTime());
  }
}
