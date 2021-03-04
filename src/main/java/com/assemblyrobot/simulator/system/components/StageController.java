package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.generators.ErrorOccurrenceGenerator;
import com.assemblyrobot.simulator.core.metrics.MetricsCollector;
import com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import com.assemblyrobot.simulator.system.stages.FixStage;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class StageController {
  @Getter private final EventQueue eventQueue;
  private final HashMap<Long, Material> materials = new HashMap<>();
  private final HashMap<Long, Tracker> trackers = new HashMap<>();
  private final ArrayList<Material> transferQueue = new ArrayList<>();
  private final AssemblyStage assemblyStage = new AssemblyStage(this);
  private final ErrorCheckStage errorCheckStage = new ErrorCheckStage(this);
  private final FixStage fixStage = new FixStage(this);
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
    materials.put(material.getId(), material);

    val tracker = new Tracker(material.getId());
    trackers.put(tracker.getTrackerId(), tracker);

    sendToNextStage(material);

    metricsCollector.incrementMetric(Metrics.TOTAL_MATERIAL_AMOUNT.getMetricName());
  }

  public void registerOutgoingMaterial(long id) {
    metricsCollector.incrementMetric(Metrics.TOTAL_ASSEMBLED_AMOUNT.getMetricName());
  }

  // tracker contains an arraylist of data the id is the same as material id
  private void addTrackingData(@NonNull Tracker tracker) {
    trackers.put(tracker.getTrackerId(), tracker);
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
          logger.trace("Material {}: Progressing to Assembly.", material.getId());
          assemblyStage.addToStationQueue(material);
        }
        case ERROR_CHECK -> {
          logger.trace("Material {}: Progressing to ErrorCheck.", material.getId());
          errorCheckStage.addToStationQueue(material);
        }
        case FIX -> {
          logger.trace("Material {}: Progressing to Fix.", material.getId());
          fixStage.addToStationQueue(material);
        }
        case DEPART -> {
          logger.trace("Material {}: Departing.", material.getId());
          registerOutgoingMaterial(material.getId());
        }
      }
    }
  }

  private StageID getNextStage(@NonNull Material material) {
    val materialId = material.getId();
    val tracker = trackers.get(materialId);
    val stationMetrics = tracker.getStationMetrics();
    StageID currentStageId = null;

    try {
      currentStageId = Iterables.getLast(stationMetrics).getStageId();
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
      @NonNull Material material, @NonNull MaterialMetricsCollector metrics) {
    val tracker = trackers.get(material.getId());
    tracker.addMetrics(metrics);
    addTrackingData(tracker);
    material.reset();
    transferQueue.add(material);
    eventQueue.schedule(new Event(Clock.getInstance().getCurrentTick() + 1, EventType.TRANSFER));
  }
}
