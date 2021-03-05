package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector;
import java.util.HashMap;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract super class for all station classes. Contains methods to control a {@link Material}'s
 * flow in a {@link Station}
 */
public abstract class Station extends TickAdvanceListener {

  private final StageController stageController;
  private final HashMap<Long, MaterialMetricsCollector> materialsInProcessing = new HashMap<>();
  private final String stationId;
  private Material currentMaterial;
  private long busyTimeRemaining = 0;
  private static final Logger logger = LogManager.getLogger();

  private final MetricsCollector metricsCollector;

  @RequiredArgsConstructor
  private enum Metrics {
    STATION_MATERIAL_AMOUNT("station_entered_material_amount"),
    STATION_PROCESSED_AMOUNT("station_exited_material_amount"),
    STATION_BUSY_TIME("station_busy_time"),
    STATION_TOTAL_PASSTHROUGH_TIME("station_total_passthrough_time");

    @Getter private final String metricName;
  }

  @Getter(AccessLevel.PROTECTED)
  private final PriorityQueue<Material> materialQueue = new PriorityQueue<>();

  public Station(StageController stageController, String stationId) {
    this.stageController = stageController;
    this.stationId = stationId;
    metricsCollector = new MetricsCollector(stationId, getClass().getSuperclass().getName());
  }

  /**
   * Gets the processing duration of the {@link Station} from a corresponding generator.
   *
   * @return {@link Long}
   */
  protected abstract long getProcessingTime();

  /**
   * Adds the {@link Material} to the {@link Station}'s queue {@link Station#materialQueue}. Also
   * collects related data.
   *
   * @param material {@link Material} to add to the {@link Station#materialQueue}
   * @param stageId ID of the {@link Stage} the {@link Material} is currently in
   */
  public void addToStationQueue(@NonNull Material material, @NonNull StageID stageId) {
    val materialId = material.getId();
    val materialMetrics = new MaterialMetricsCollector(stageId, stationId, materialId);
    val currentTick = getCurrentTick();

    materialQueue.add(material);
    material.setQueueStartTime(currentTick);
    materialMetrics.setQueueStartTime(currentTick);
    materialsInProcessing.put(materialId, materialMetrics);

    metricsCollector.incrementMetric(Metrics.STATION_MATERIAL_AMOUNT.getMetricName());
    processMaterial();
  }

  /** Starts the processing of the {@link Material}. Also collects related data. */
  protected void processMaterial() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = materialQueue.poll();
      currentMaterial = next;
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

      val currentTick = getCurrentTick();
      currentMaterial.setQueueEndTime(currentTick);
      currentMaterial.setProcessingStartTime(currentTick);

      val currentMaterialId = currentMaterial.getId();
      val currentMetrics = materialsInProcessing.get(currentMaterialId);
      currentMetrics.setQueueEndTime(currentTick);
      currentMetrics.setProcessingStartTime(currentTick);
      materialsInProcessing.put(currentMaterialId, currentMetrics);

      logger.trace(
          "Starting processing of {}. Processing will continue for {} ticks.",
          next,
          processingTime);
    }
  }

  /**
   * Checks if {@link Station} is currently processing another {@link Material}.
   *
   * @return {@link Boolean}
   */
  protected boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  /**
   * Checks if a {@link Material} can be pulled from the {@link Station#materialQueue}.
   *
   * @return {@link Boolean}
   */
  protected boolean canPull() {
    return !isBusy() && materialQueue.size() > 0;
  }

  /**
   * Determines the status of the {@link Material} and coordinates its processing accordingly. Also
   * collects related data.
   *
   * @param ticksAdvanced The amount of ticks that the clock has moved forward by.
   */
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
        // Update station metrics
        metricsCollector.incrementMetric(
            Metrics.STATION_BUSY_TIME.getMetricName(), getProcessingTime());
        metricsCollector.incrementMetric(Metrics.STATION_PROCESSED_AMOUNT.getMetricName());
        metricsCollector.incrementMetric(
            Metrics.STATION_TOTAL_PASSTHROUGH_TIME.getMetricName(),
            currentMaterial.getTotalPassthroughTime());

        // Update material metrics
        val currentTick = getCurrentTick();
        val currentMaterialId = currentMaterial.getId();
        val currentMetrics = materialsInProcessing.get(currentMaterialId);

        currentMaterial.setProcessingEndTime(currentTick);
        currentMetrics.setProcessingEndTime(currentTick);
        currentMetrics.updateMetrics();

        logger.trace("Processing for material {} finished.", currentMaterial);

        stageController.onChildQueueDepart(currentMaterial, currentMetrics);
        materialsInProcessing.remove(currentMaterialId);
      }

      busyTimeRemaining = newBusyTime;
    } else {
      processMaterial();
    }
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
  }
}
