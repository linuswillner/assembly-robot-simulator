package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.controllers.StageController;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Station extends TickAdvanceListener {

  private final StageController stageController;
  private MaterialStationData currentStationData;
  private Material currentMaterial;
  private long busyTimeRemaining = 0;
  private static int nextFreeId = 1;
  private static final Logger logger = LogManager.getLogger();

  private final MetricsCollector metricsCollector =
      new MetricsCollector(
          "%s-%d".formatted(getClass().getSimpleName(), nextFreeId),
          getClass().getSuperclass().getName());

  @Getter(AccessLevel.PROTECTED)
  private final PriorityQueue<Material> materialQueue = new PriorityQueue<>();

  public Station(StageController stageController) {
    this.stageController = stageController;
    nextFreeId++;
  }

  public static void resetId() {
    nextFreeId = 1;
  }

  protected boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  protected boolean canPull() {
    return !isBusy() && materialQueue.size() > 0;
  }

  public void addToStationQueue(
      @NonNull Material material, @NonNull MaterialStationData stationData) {
    currentStationData = stationData;
    materialQueue.add(material);
    stationData.setQueueStartTime(material.getQueueStartTime());
    poll();
  }

  protected Material pullFromStationQueue() {
    return materialQueue.poll();
  }

  protected void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromStationQueue();
      currentMaterial = next;
      stageController.registerMaterialProcessing(next.getId());
      val processingTime = getProcessingTime();

      // The PROCESSING_COMPLETE event has no function beyond stopping the clock at the moment where
      // busy time reaches 0 so that we can call onChildQueueDepart()
      stageController
          .getEventQueue()
          .schedule(
              new Event(
                  Clock.getInstance().getCurrentTick() + processingTime,
                  EventType.PROCESSING_COMPLETE));

      busyTimeRemaining = processingTime;

      currentStationData.setProcessingStartTime(currentMaterial.getProcessingStartTime());
      currentStationData.setQueueEndTime(currentMaterial.getQueueEndTime());

      logger.trace(
          "Starting processing of {}. Processing will continue for {} ticks.",
          next,
          processingTime);
    }
  }

  protected abstract long getProcessingTime();

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
        currentStationData.setProcessingEndTime(currentMaterial.getProcessingEndTime());
        stageController.onChildQueueDepart(currentMaterial, currentStationData);
        logger.trace("Processing for material {} finished.", currentMaterial);
      }

      busyTimeRemaining = newBusyTime;
    } else {
      poll();
    }
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
  }
}
