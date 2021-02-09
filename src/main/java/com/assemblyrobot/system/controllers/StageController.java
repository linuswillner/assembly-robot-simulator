package com.assemblyrobot.system.controllers;

import com.assemblyrobot.system.core.Material;
import com.assemblyrobot.simulator.core.clock.Clock;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class StageController {
  @Getter private final HashMap<Integer, Material> cache = new HashMap<>();

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
