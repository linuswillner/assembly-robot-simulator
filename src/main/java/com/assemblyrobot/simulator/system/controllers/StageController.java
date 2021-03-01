package com.assemblyrobot.simulator.system.controllers;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.generators.ErrorOccurrenceGenerator;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Tracker;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
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

  private final AssemblyStage assemblyStage = new AssemblyStage(this);
  private final ErrorCheckStage errorCheckStage = new ErrorCheckStage(this);

  private static final Logger logger = LogManager.getLogger();
  private final MetricsCollector metricsCollector =
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
    val nextStageId = getNextStage(material);

    if (nextStageId == null) {
      logger.warn("Material {}: Not progressing anywhere.", material.getId());
    } else {
      switch (nextStageId) {
        case ASSEMBLY -> {
          assemblyStage.addToStationQueue(material);
          logger.trace("Material {}: Progressing to Assembly.", material.getId());
        }
        case ERROR_CHECK -> {
          errorCheckStage.addToStationQueue(material);
          logger.trace("Material {}: Progressing to ErrorCheck.", material.getId());
        }
        case FIX -> {

          logger.trace("Material {}: Progressing to Fix.", material.getId());
        }
        case DEPART -> {
          registerOutgoingMaterial(material.getId());
          logger.trace("Material {}: Departing.", material.getId());
        }
      }
    }
  }

  private StageID getNextStage(@NonNull Material material) {
    val materialId = material.getId();
    val tracker = trackerCache.get(materialId);
    val stationDataList = tracker.getDataForStations();
    StageID currentStageId = null;

    try {
      currentStageId = Iterables.getLast(stationDataList).getStageId();
    } catch (NoSuchElementException e) {
      logger.trace("Material {}: No station data logged yet.", materialId);
    }

    if (currentStageId == null) {
      logger.trace("Material {}: Current stage = Arrival, next stage = Assembly", materialId);
      return StageID.ASSEMBLY;
    } else {
      switch (currentStageId) {
        case ASSEMBLY -> {
          logger.trace(
              "Material {}: Current stage = {}, next stage = ErrorCheck", materialId,
              currentStageId);
          return StageID.ERROR_CHECK;
        }
        case ERROR_CHECK -> {
          // Have to use explicit cast to boolean here, lombok doesn't like val in switch blocks
          boolean shouldHaveError = ErrorOccurrenceGenerator.getInstance().shouldHaveError();

          logger.trace("Material {}: Has error? {}", materialId, shouldHaveError ? "Yes" : "No");
          logger.trace("Material {}: Current stage = {}, next stage = {}",  materialId,
              currentStageId, shouldHaveError ? "FIX" : "DEPART");

          return shouldHaveError ? StageID.FIX : StageID.DEPART;
        }
        case FIX -> {
          logger.trace("Material {}: Current stage = {}, next stage = Depart", materialId,
              currentStageId);
          return StageID.DEPART;
        }
        default -> {
          logger.warn("Material {}: Stage {} has no next stage defined.", materialId, currentStageId);
          return null;
        }
      }
    }
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
