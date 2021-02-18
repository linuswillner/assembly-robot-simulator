package com.assemblyrobot.simulator.system.controllers;

import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.system.components.Tracker;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import java.util.HashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class StageController {
  @Getter private final HashMap<Long, Material> materialCache = new HashMap<>();
  @Getter private final HashMap<Long, Tracker> trackerCache = new HashMap<>();
  private final EventQueue eventQueue;
  // TODO: Note that these are placeholder variables. The amount of stations that will be created
  // will be asked from the user in the UI. Implement later.
  private int assemblyStationAmount = 1;
  private int errorCheckStationAmount = 1;
  private final AssemblyStage assemblyStage = new AssemblyStage(assemblyStationAmount, this);
  private final ErrorCheckStage errorCheckStage =
      new ErrorCheckStage(errorCheckStationAmount, this);

  public void registerIncomingMaterial() {
    Material material = new Material();
    material.setProcessingStartTime(getCurrentTick());
    material.setQueueStartTime(getCurrentTick());
    Tracker tracker = new Tracker(material.getId());
    trackerCache.put(tracker.getMaterialid(), tracker);
    materialCache.put(material.getId(), material);
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
  }

  // todo make addtoq methods to every stage
  public void addToAssemblyQueue(Material material) {
    assemblyStage.addToStationQueue(material);
  }

  public void addToErrorStageQueue(Material material) {
    errorCheckStage.addToStationQueue(material);
  }
}
