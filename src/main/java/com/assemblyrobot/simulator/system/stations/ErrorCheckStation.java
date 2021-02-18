package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ErrorCheckStation extends Station implements Comparable<ErrorCheckStation> {

  @Getter private final PriorityQueue<Material> materialQueue = new PriorityQueue<>();
  private final StageController stageController;
  private MaterialStationData stationData;
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private long busyTimeRemaining = 0;

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private Material currentMaterial = null;

  public ErrorCheckStation(@NonNull ErrorCheckStage stage) {
    stageController = stage.getStageController();
  }

  // TODO: Note this is a placeholder method. Change after all time generators have been
  // implemented.
  @Override
  protected long getProcessingTime() {
    return 5;
  }
  /* @Override
  protected long getProcessingTime() {
    return ErrorCheckTimeGenerator.getInstance().nextLong();
  }*/

  // Busy logic

  protected boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  protected boolean canPull() {
    return !isBusy() && materialQueue.size() > 0;
  }

  // Queue operations

  public void addToStationQueue(
      @NonNull Material material, @NonNull MaterialStationData stationData) {
    this.stationData = stationData;
    materialQueue.add(material);
    stationData.setQueueStartTime(material.getQueueStartTime());
  }

  protected Material pullFromStationQueue() {
    return materialQueue.poll();
  }

  protected void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromStationQueue();
      setCurrentMaterial(next);
      stageController.registerMaterialProcessing(next.getId());
      val processingTime = getProcessingTime();

      setBusyTimeRemaining(processingTime);

      stationData.setProcessingStartTime(currentMaterial.getProcessingStartTime());
      stationData.setQueueEndTime(currentMaterial.getQueueEndTime());

      // The PROCESSING_COMPLETE event has no function beyond stopping the clock at the moment where
      // busy time reaches 0 so that we can call onChildQueueDepart()
      stageController
          .getEventQueue()
          .schedule(
              new Event(
                  Clock.getInstance().getCurrentTick() + processingTime,
                  EventType.PROCESSING_COMPLETE));

      logger.trace(
          "Starting processing of {}. Processing will continue for {} ticks.",
          next,
          processingTime);
    }
  }

  // Handles ordering the PriorityQueue inside AssemblyStage

  @Override
  public int compareTo(@NonNull ErrorCheckStation station) {
    return Integer.compare(this.materialQueue.size(), station.getMaterialQueue().size());
  }

  // Tick advance listener methods

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    if (isBusy()) {
      val newBusyTime = busyTimeRemaining - ticksAdvanced;

      logger.trace(
          "Advanced {} ticks. Busy time reduced from {} to {}.",
          ticksAdvanced,
          busyTimeRemaining,
          newBusyTime);

      if (newBusyTime == 0) {
        stationData.setProcessingEndTime(currentMaterial.getProcessingEndTime());
        stageController.onChildQueueDepart(currentMaterial, stationData);
        logger.trace("Processing for material {} finished.", currentMaterial);
      }

      setBusyTimeRemaining(newBusyTime);
    } else {
      poll();
    }
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
  }
}
