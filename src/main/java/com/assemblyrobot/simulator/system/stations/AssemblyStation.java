package com.assemblyrobot.simulator.system.stations;

import com.assemblyrobot.simulator.core.generators.AssemblyTimeGenerator;
import com.assemblyrobot.simulator.core.metrics.MaterialStationData;
import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.system.components.Station;
import com.assemblyrobot.simulator.system.controllers.StageController;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AssemblyStation extends Station implements Comparable<AssemblyStation>{

  @Getter private PriorityQueue<Material> stationQueue = new PriorityQueue<>();
  private final StageController stageController;
  @Getter private MaterialStationData stationData;
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private long busyTimeRemaining = 0;

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private Material currentMaterial = null;

  public AssemblyStation(StageController stageController){
    this.stageController = stageController;
  }

  @Override
  protected long getProcessingTime() {
    return AssemblyTimeGenerator.getInstance().nextLong();
  }

  // Busy logic

  public boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  private boolean canPull() {
    return !isBusy() && stationQueue.size() > 0;
  }

  // Queue operations

  //Interface overload method
  @Override
  public void addToQueue(Material material) {

  }

  @Override
  public void addToQueue(Material material, MaterialStationData stationData) {
    this.stationData = stationData;
    stationQueue.add(material);
    stationData.setQueueStartTime(material.getQueueStartTime());

  }

  private Material pullFromStationQueue() {
    return stationQueue.poll();
  }

  // TODO: check if possible DRY violation
  public void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromStationQueue();
      setCurrentMaterial(next);
      stageController.registerMaterialProcessing(next.getId());
      val processingTime = getProcessingTime();

      setBusyTimeRemaining(processingTime);

      stationData.setProcessingStartTime(currentMaterial.getProcessingStartTime());
      stationData.setQueueEndTime(currentMaterial.getQueueEndTime());

      logger.trace("Starting processing of {}. Processing will continue for {} ticks.", next, processingTime);
    }
  }

  @Override
  public void onChildQueueDepart(Material material, MaterialStationData data){
  }

  // Handles ordering the PriorityQueue inside AssemblyStage

  public int compareTo(AssemblyStation station){
    return Integer.compare(this.stationQueue.size(), station.getStationQueue().size());
  }

  // Tick advance listener methods

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    if (isBusy()) {
      val newBusyTime = busyTimeRemaining - ticksAdvanced;

      logger.trace("Advanced {} ticks. Busy time reduced from {} to {}.", ticksAdvanced, busyTimeRemaining, newBusyTime);

      if (newBusyTime == 0) {
        logger.trace("Processing for material {} finished.", currentMaterial);
      }

      setBusyTimeRemaining(newBusyTime);
    }
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
  }

}
