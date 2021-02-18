package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.metricscollectors.StationMetricsCollector;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class AssemblyStation extends Station implements Comparable<AssemblyStation> {

  @Getter private PriorityQueue<Material> stationQueue = new PriorityQueue<>();
  private final AssemblyStage stage;
  private final StageController stageController;
  @Getter private MaterialStationData stationData;
  @Getter private StationMetricsCollector stationMetricsCollector;
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private long busyTimeRemaining = 0;

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private Material currentMaterial = null;

  public AssemblyStation(AssemblyStage stage) {
    this.stage = stage;
    stationMetricsCollector = new StationMetricsCollector(this);
    stageController = stage.getStageController();
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }

  // Busy logic

  public boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  protected boolean canPull() {
    return !isBusy() && stationQueue.size() > 0;
  }

  // Queue operations

  public void addToStationQueue(Material material, MaterialStationData stationData) {
    this.stationData = stationData;
    stationQueue.add(material);
    stationData.setQueueStartTime(material.getQueueStartTime());
  }

  protected Material pullFromStationQueue() {
    return stationQueue.poll();
  }

  public void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromStationQueue();
      setCurrentMaterial(next);
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

      setBusyTimeRemaining(processingTime);

      stationData.setProcessingStartTime(currentMaterial.getProcessingStartTime());
      stationData.setQueueEndTime(currentMaterial.getQueueEndTime());

      logger.trace(
          "Starting processing of {}. Processing will continue for {} ticks.",
          next,
          processingTime);
    }
  }

  // Handles ordering the PriorityQueue inside AssemblyStage

  @Override
  public int compareTo(@NotNull AssemblyStation station) {
    return Integer.compare(this.stationQueue.size(), station.getStationQueue().size());
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
