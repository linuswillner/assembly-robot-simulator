package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.shared.constants.StageID;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.events.TransferEvent;
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
  private final HashMap<Long, Tracker> trackers = new HashMap<>();
  private final ArrayList<Material> transferQueue = new ArrayList<>();
  private final AssemblyStage assemblyStage = new AssemblyStage(this);
  private final ErrorCheckStage errorCheckStage = new ErrorCheckStage(this);
  private final FixStage fixStage = new FixStage(this);
  private static final Logger logger = LogManager.getLogger();
  private final MetricsCollector metricsCollector =
      new MetricsCollector(getClass().getSimpleName(), getClass().getName());

  @RequiredArgsConstructor
  public enum Metrics {
    TOTAL_MATERIAL_AMOUNT("total_entered_material_amount"),
    TOTAL_ASSEMBLED_AMOUNT("total_exited_material_amount");

    @Getter private final String metricName;
  }

  /**
   * Puts {@link Material}s and {@link Tracker}s into their respective data stores, and propagates
   * transfers via {@link StageController#sendToNextStage}. Increments total material amount for
   * metrics.
   */
  public void registerIncomingMaterial() {
    val material = new Material();
    val tracker = new Tracker(material.getId());
    addTrackingData(tracker);
    sendToNextStage(material);

    metricsCollector.incrementMetric(Metrics.TOTAL_MATERIAL_AMOUNT.getMetricName());
  }

  /**
   * Logs a {@link Tracker} for a {@link Material} in {@link StageController#trackers}.
   *
   * @param tracker {@link Tracker} to log.
   */
  private void addTrackingData(@NonNull Tracker tracker) {
    trackers.put(tracker.getTrackerId(), tracker);
  }

  /** Transfers all materials in the transfer queue to their appropriate destinations. */
  public void transferAll() {
    transferQueue.forEach(this::sendToNextStage);
    transferQueue.clear();
  }

  /**
   * Internal callback for executing transfers of {@link Material}s.
   *
   * @param material {@link Material}
   */
  private void sendToNextStage(@NonNull Material material) {
    val currentStage = material.getCurrentStage();
    val shouldHaveError = currentStage == StageID.ERROR_CHECK && material.getError() != null;

    val nextStage = getNextStage(material, shouldHaveError);
    val materialId = material.getId();
    material.setNextStage(nextStage);

    logger.trace(
        "Material {}: Current stage = {}, next stage = {}", materialId, currentStage, nextStage);

    if (nextStage == null) {
      logger.warn("Material {}: Not progressing anywhere.", material.getId());
    } else {
      switch (nextStage) {
        case ASSEMBLY -> assemblyStage.addToStationQueue(material, null);
        case ERROR_CHECK -> errorCheckStage.addToStationQueue(material, null);
        case FIX -> fixStage.addToStationQueue(material, material.getError());
        case DEPART -> metricsCollector.incrementMetric(Metrics.TOTAL_ASSEMBLED_AMOUNT.getMetricName());
      }
    }
  }

  /**
   * Internal callback for determining the next stage of a {@link Material}.
   *
   * @param material {@link Material}
   * @param hasError Whether this material has an error, and should be sent to the {@link FixStage}
   *     before departing.
   * @return {@link StageID}
   */
  private StageID getNextStage(@NonNull Material material, boolean hasError) {
    val materialId = material.getId();
    val currentStageId = getLastStageOfTracker(trackers.get(materialId));

    // Having to do this null check here to avoid NullPointerExceptions, hence why we can't just
    // fallthrough to ASSEMBLY
    if (currentStageId == null) {
      return StageID.ASSEMBLY;
    } else {
      switch (currentStageId) {
        case ASSEMBLY -> {
          return StageID.ERROR_CHECK;
        }
        case ERROR_CHECK -> {
          return hasError ? StageID.FIX : StageID.DEPART;
        }
        case FIX -> {
          return StageID.DEPART;
        }
        default -> {
          return null;
        }
      }
    }
  }

  /**
   * Gets the last known {@link StageID} of a {@link Tracker}.
   *
   * @param tracker {@link Tracker}
   * @return {@link StageID} or null if none logged yet-
   */
  private StageID getLastStageOfTracker(Tracker tracker) {
    try {
      return Iterables.getLast(tracker.getStationMetrics()).getStageId();
    } catch (NoSuchElementException e) {
      logger.trace("Material {}: No station data logged yet.", tracker.getTrackerId());
      return null;
    }
  }

  /**
   * Callback called by {@link Station}s when {@link Material}s complete processing and depart their
   * queues. Triggers scheduling of {@link TransferEvent}s (Type {@link EventType#TRANSFER}).
   *
   * @param material {@link Material} to add to the transfer queue.
   * @param metrics Populated {@link MaterialMetricsCollector} to add to the tracker.
   *     <p>Prepares material for transfer method by adding it to {@link
   *     StageController#transferQueue} for {@link StageController#transferAll()}. Adds {@link
   *     MaterialMetricsCollector} to {@link Tracker} and uses {@link
   *     StageController#addTrackingData(Tracker)} to add it to{@link StageController#trackers}
   *     HashMap
   */
  public void onChildQueueDepart(
      @NonNull Material material, @NonNull MaterialMetricsCollector metrics) {
    val tracker = trackers.get(material.getId());
    tracker.addMetrics(metrics);
    addTrackingData(tracker);
    material.reset();
    transferQueue.add(material);

    val shouldHaveError =
        material.getCurrentStage() == StageID.ERROR_CHECK
            && ErrorOccurrenceGenerator.getInstance().shouldHaveError();
    val error = shouldHaveError ? ErrorOccurrenceGenerator.getInstance().nextError() : null;

    if (shouldHaveError) {
      material.setError(error);
    }

    val hasError = material.getCurrentStage() == StageID.FIX;

    if (shouldHaveError) {
      material.setError(error);
    }

    eventQueue.schedule(
        new TransferEvent(
            Clock.getInstance().getCurrentTick() + 1,
            EventType.TRANSFER,
            material.getCurrentStage(),
            getNextStage(material, shouldHaveError),
            hasError ? material.getError() : error));
  }
}
