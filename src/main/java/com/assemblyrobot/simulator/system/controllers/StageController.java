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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class StageController {
  @Getter private final HashMap<Long, Material> materialCache = new HashMap<>();
  @Getter private final HashMap<Long, Tracker> trackerCache = new HashMap<>();
  @Getter private final ArrayList<Material> transferQueue = new ArrayList<>();
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
    Material material = new Material();
    material.setProcessingStartTime(getCurrentTick());
    material.setQueueStartTime(getCurrentTick());
    Tracker tracker = new Tracker(material.getId());
    trackerCache.put(tracker.getMaterialid(), tracker);
    materialCache.put(material.getId(), material);
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
  public void addTrackingData(Tracker tracker) {
    trackerCache.put(tracker.getMaterialid(), tracker);
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
  }

  public void onChildQueueDepart(Material material, MaterialStationData stationData) {
    Tracker tracker = trackerCache.get(material.getId());
    tracker.addData(stationData);
    addTrackingData(tracker);

    transferQueue.add(material);
    eventQueue.schedule(new Event(Clock.getInstance().getCurrentTick() + 1, EventType.TRANSFER));
  }

  private void addToAssemblyStageQueue(Material material) {
    assemblyStage.addToStationQueue(material);
  }

  private void addToErrorCheckStageQueue(Material material) {
    errorCheckStage.addToStationQueue(material);
  }

  private StageID getNextStage(Material material) {
    // Get stageID from tracker
    long materialId = material.getId();
    logger.trace("Material: " + materialId + " Implementing getNextStage()");
    Tracker tracker = trackerCache.get(materialId);
    ArrayList<MaterialStationData> stationDataList = tracker.getDataForStations();
    StageID currentStageId = null;
    try{
     currentStageId = stationDataList.get(stationDataList.size() - 1).getStageId();
    }catch(IndexOutOfBoundsException e){
      logger.warn("Material: " + materialId + " StationDataList is empty.");
    }


    // TODO: implement FIX/DEPART stage progression
    if (currentStageId == null) {
      logger.trace("Material: " + materialId + " Next stage will be Assembly.");
      return StageID.ASSEMBLY;
    } else if (currentStageId == StageID.ASSEMBLY) {
      logger.trace("Material: " + materialId + " Next stage will be ErrorCheck.");
      return StageID.ERROR_CHECK;
    } else if (currentStageId == StageID.ERROR_CHECK) {
      logger.trace("Material: " + materialId + " Next stage will be Fix.");
      return StageID.FIX;
    } else {
      logger.warn("Material: " + materialId + " StageID cannot be null.");
      return null;
    }
  }

  private void sendToNextStage(Material material) {
    try{
      transferQueue.remove(transferQueue.indexOf(material.getId()));
    } catch (IndexOutOfBoundsException e) {
      logger.warn("Material: " + material.getId() + " TransferQueue is empty.");
    }
    StageID stageId = getNextStage(material);

    if (stageId == StageID.ASSEMBLY) {
      addToAssemblyStageQueue(material);
      logger.trace("Material: " + material.getId() + " progressing to Assembly.");
    } else if (stageId == StageID.ERROR_CHECK) {
      addToErrorCheckStageQueue(material);
      logger.trace("Material: " + material.getId() + " progressing to ErrorCheck.");
    } else if (stageId == StageID.FIX){
      logger.trace("Material: " + material.getId() + " progressing to Fix.");
    } else if (stageId == StageID.DEPART) {
      registerOutgoingMaterial(material.getId());
      logger.trace("Material: " + material.getId() + " departing.");
    } else {
      logger.warn("Material: " + material.getId() + " Material not progressing anywhere.");
    }
  }

  public void transferAll() {
    transferQueue.forEach(this::sendToNextStage);
  }
}
