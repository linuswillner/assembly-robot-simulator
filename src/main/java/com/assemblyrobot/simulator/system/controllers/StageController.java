package com.assemblyrobot.simulator.system.controllers;

import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.system.components.Tracker;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.checkerframework.checker.units.qual.A;

@RequiredArgsConstructor
public class StageController{
  @Getter private final HashMap<Integer, Material> materialCache = new HashMap<>();
  @Getter private final HashMap<Integer, Tracker> trackerCache = new HashMap<>();
  //TODO: Note that these are placeholder variables. The amount of stations that will be created will be asked from the user in the UI. Implement later.
  private int assemblyStationAmount = 1;
  private int errorCheckStationAmount = 1;
  private final AssemblyStage assemblyStage = new AssemblyStage(assemblyStationAmount, this);
  private final ErrorCheckStage errorCheckStage = new ErrorCheckStage(errorCheckStationAmount, this);



  public void registerIncomingMaterial(@NonNull Material material) {
    material.setProcessingStartTime(getCurrentTick());
    material.setQueueStartTime(getCurrentTick());
    materialCache.put(material.getId(), material);
  }

  public void registerMaterialProcessing(int id) {
    val material = materialCache.get(id);
    material.setQueueEndTime(getCurrentTick());
    materialCache.put(id, material);
  }

  public void registerOutgoingMaterial(int id) {
    val material = materialCache.get(id);
    material.setProcessingEndTime(getCurrentTick());
    materialCache.put(id, material);
  }

  //tracker contains an arraylist of data the id is the same as material id
  public void addTrackingData(int id, Tracker tracker){
    trackerCache.put(id,tracker);
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
  }


/*
  TODO Refractor this method to work with new event queue

  eventQueue.schedule(
   new Event(Clock.getInstance().getCurrentTick() + processingTime, EventType.DEPARTURE));
*/

  public EventQueue getQueue(){
    return EventQueue.getInstance();
  }

  //todo make addtoq methods to every stage
  public void addToAssemblyQueue(Material material){
    assemblyStage.addToAssemblyStationQueue(material);
  }
  public void addToErrorStageQueue(Material material){
    errorCheckStage.addToErrorCheckStationQueue(material);
  }
}
