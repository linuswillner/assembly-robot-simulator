package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import java.util.HashMap;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Station extends TickAdvanceListener {

  private final StageController stageController;
  private final HashMap<Long, MaterialStationData> materialsInProcessing = new HashMap<>();
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

  protected boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  protected boolean canPull() {
    return !isBusy() && materialQueue.size() > 0;
  }

  public void addToStationQueue(@NonNull Material material, @NonNull StageID stageId) {
    val materialId = material.getId();
    val stationData = new MaterialStationData(stageId, stationId, materialId);
    val currentTick = getCurrentTick();

    materialQueue.add(material);
    material.setQueueStartTime(currentTick);
    stationData.setQueueStartTime(currentTick);
    materialsInProcessing.put(materialId, stationData);

    metricsCollector.incrementMetric(Metrics.STATION_MATERIAL_AMOUNT.getMetricName());
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

      val currentTick = getCurrentTick();
      currentMaterial.setQueueEndTime(currentTick);
      currentMaterial.setProcessingStartTime(currentTick);

      val currentMaterialId = currentMaterial.getId();
      val currentStationData = materialsInProcessing.get(currentMaterialId);
      currentStationData.setQueueEndTime(currentTick);
      currentStationData.setProcessingStartTime(currentTick);
      materialsInProcessing.put(currentMaterialId, currentStationData);

      logger.trace(
          "Starting processing of {}. Processing will continue for {} ticks.",
          next,
          processingTime);
    }
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
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
        val currentStationData = materialsInProcessing.get(currentMaterialId);

        currentMaterial.setProcessingEndTime(currentTick);
        currentStationData.setProcessingEndTime(currentTick);
        currentStationData.updateMetrics();

        logger.trace("Processing for material {} finished.", currentMaterial);

        stageController.onChildQueueDepart(currentMaterial, currentStationData);
        materialsInProcessing.remove(currentMaterialId);
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
