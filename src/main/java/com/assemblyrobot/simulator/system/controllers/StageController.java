package com.assemblyrobot.simulator.system.controllers;

import com.assemblyrobot.simulator.system.components.Material;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.system.stages.AssemblyStage;
import com.assemblyrobot.simulator.system.stages.ErrorCheckStage;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.checkerframework.checker.units.qual.A;

@RequiredArgsConstructor
public class StageController {
  @Getter private final HashMap<Integer, Material> cache = new HashMap<>();
  //TODO: Note that these are placeholder variables. The amount of stations that will be created will be asked from the user in the UI. Implement later.
  private int assemblyStationAmount = 1;
  private int errorCheckStationAmount = 1;
  private final AssemblyStage assemblyStage = new AssemblyStage(assemblyStationAmount, this);
  private final ErrorCheckStage errorCheckStage = new ErrorCheckStage(errorCheckStationAmount, this);
  public void registerIncomingMaterial(@NonNull Material material) {
    material.setProcessingStartTime(getCurrentTick());
    material.setQueueStartTime(getCurrentTick());
    cache.put(material.getId(), material);
  }

  public void registerMaterialProcessing(int id) {
    val material = cache.get(id);
    material.setQueueEndTime(getCurrentTick());
    cache.put(id, material);
  }

  public void registerOutgoingMaterial(int id) {
    val material = cache.get(id);
    material.setProcessingEndTime(getCurrentTick());
    cache.put(id, material);
  }

  private long getCurrentTick() {
    return Clock.getInstance().getCurrentTick();
  }
}

/* eventQueue.schedule(
          new Event(Clock.getInstance().getCurrentTick() + processingTime, EventType.DEPARTURE));
*/
