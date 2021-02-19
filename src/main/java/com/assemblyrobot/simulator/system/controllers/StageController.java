package com.assemblyrobot.simulator.system.controllers;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Tracker;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.assemblyrobot.simulator.system.stages.StageID;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class StageController {
  @Getter(AccessLevel.PRIVATE)
  private final HashMap<Long, Material> materialCache = new HashMap<>();

  @Getter(AccessLevel.PRIVATE)
  private final HashMap<Long, Tracker> trackerCache = new HashMap<>();

  @Getter(AccessLevel.PRIVATE)
  private final ArrayList<Material> transferQueue = new ArrayList<>();

  @Getter private final EventQueue eventQueue;
  // TODO: Note that these are placeholder variables. The amount of stations that will be created
  // will be asked from the user in the UI. Implement later.
  private int assemblyStationAmount = 1;
  private int errorCheckStationAmount = 1;
  private final AssemblyStage assemblyStage = new AssemblyStage(assemblyStationAmount, this);
  private final ErrorCheckStage errorCheckStage =
      new ErrorCheckStage(errorCheckStationAmount, this);
  private static final Logger logger = LogManager.getLogger();
  private MetricsCollector metricsCollector =
      new MetricsCollector(getClass().getSimpleName(), getClass().getName());

  @RequiredArgsConstructor
  private enum Metrics {
    TOTAL_MATERIAL_AMOUNT("total_entered_material_amount"),
    TOTAL_ASSEMBLED_AMOUNT("total_exited_material_amount");

    @Getter private final String metricName;
  }

  public void registerIncomingMaterial() {
    val material = new Material();
    material.setProcessingStartTime(getCurrentTick());
    material.setQueueStartTime(getCurrentTick());
    materialCache.put(material.getId(), material);

    val tracker = new Tracker(material.getId());
    trackerCache.put(tracker.getTrackerId(), tracker);
    
    sendToNextStage(material);
    
    metricsCollector.incrementMetric(Metrics.TOTAL_MATERIAL_AMOUNT.getMetricName());
  }

  public void registerMaterialProcessing(long id) {
    val material = materialCache.get(id);
    material.setQueueEndTime(getCurrentTick());
    materialCache.put(id, material);
  }

  public void registerOutgoingMaterial(long id) {
    val material = materialCache.get(id);
    material.setProcessingEndTime(getCurrentTick());
    materialCache.put(id, material);

    metricsCollector.incrementMetric(Metrics.TOTAL_ASSEMBLED_AMOUNT.getMetricName());
  }

  // tracker contains an arraylist of data the id is the same as material id
  private void addTrackingData(@NonNull Tracker tracker) {
    trackerCache.put(tracker.getTrackerId(), tracker);
  }

  public void transferAll() {
    transferQueue.forEach(this::sendToNextStage);
    transferQueue.clear();
  }

  private void sendToNextStage(@NonNull Material material) {
    StageID stageId = getNextStage(material);

    if (stageId == StageID.ASSEMBLY) {
      addToAssemblyStageQueue(material);
      logger.trace("Material ID {} progressing to Assembly stage.", material.getId());
    } else if (stageId == StageID.ERROR_CHECK) {
      addToErrorCheckStageQueue(material);
      logger.trace("Material ID {} progressing to Error Check stage.", material.getId());
    } else if (stageId == StageID.FIX) {
      logger.trace("Material ID {} progressing to Error Fix stage.", material.getId());
    } else if (stageId == StageID.DEPART) {
      registerOutgoingMaterial(material.getId());
      logger.trace("Material ID {} departing.", material.getId());
    } else {
      logger.warn("Material ID {} not progressing anywhere.", material.getId());
    }
  }

  private StageID getNextStage(@NonNull Material material) {
    // Get stageID from tracker
    val materialId = material.getId();
    val tracker = trackerCache.get(materialId);
    val stationDataList = tracker.getDataForStations();
    StageID currentStageId = null;
    try {
      currentStageId = stationDataList.get(stationDataList.size() - 1).getStageId();
    } catch (IndexOutOfBoundsException e) {
      logger.trace("Material ID {}: StationDataList is empty.", materialId);
    }

    // TODO: implement FIX/DEPART stage progression
    if (currentStageId == null) {
      logger.trace(
          "Material ID {}: Current stage: {} Next stage: Assembly", materialId, currentStageId);
      return StageID.ASSEMBLY;
    } else if (currentStageId == StageID.ASSEMBLY) {
      logger.trace(
          "Material ID {}: Current stage: {} Next stage: ErrorCheck", materialId, currentStageId);
      return StageID.ERROR_CHECK;
    } else if (currentStageId == StageID.ERROR_CHECK) {
      logger.trace("Material ID {}: Current stage: {} Next stage: Fix", materialId, currentStageId);
      return StageID.FIX;
    } else {
      logger.warn("WARNING: Material ID {}: StageID cannot be null.", materialId);
      return null;
    }
  }

  private void addToAssemblyStageQueue(@NonNull Material material) {
    assemblyStage.addToStationQueue(material);
  }

  private void addToErrorCheckStageQueue(@NonNull Material material) {
    errorCheckStage.addToStationQueue(material);
  }

  public void onChildQueueDepart(
      @NonNull Material material, @NonNull MaterialStationData stationData) {
    val tracker = trackerCache.get(material.getId());
    tracker.addData(stationData);
    addTrackingData(tracker);

    transferQueue.add(material);
    eventQueue.schedule(new Event(Clock.getInstance().getCurrentTick() + 1, EventType.TRANSFER));
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
  }
}
