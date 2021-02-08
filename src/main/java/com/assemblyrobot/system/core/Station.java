package com.assemblyrobot.system.core;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.system.controllers.StationController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public abstract class Station extends TickAdvanceListener {
  @Getter private final EventQueue eventQueue;
  @Getter private final StationController controller = new StationController();
  @Getter private final MaterialQueue queue = new MaterialQueue();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private long busyTimeRemaining = 0;

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private Material currentMaterial = null;

  public Station(EventQueue eventQueue) {
    this.eventQueue = eventQueue;
  }

  // Busy logic

  public boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  private boolean canPull() {
    return !isBusy() && queue.getQueue().size() > 0;
  }

  // Queue operations

  public void addToQueue(Material material) {
    controller.registerIncomingMaterial(material);
    queue.add(material);
  }

  private Material pullFromQueue() {
    return queue.pop();
  }

  public void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromQueue();
      setCurrentMaterial(next);
      controller.registerMaterialProcessing(next.getId());
      val processingTime = getProcessingTime();

      // TODO: Refactor this; handoff to the StageController instead when implemented
      eventQueue.schedule(
          new Event(Clock.getInstance().getCurrentTick() + processingTime, EventType.DEPARTURE));

      setBusyTimeRemaining(processingTime);

      System.out.printf(
          "STATION: Starting processing of %s. Processing will continue for %d ticks.%n",
          next, processingTime);
    }
  }

  // Tick advance listener methods

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    if (isBusy()) {
      val newBusyTime = busyTimeRemaining - ticksAdvanced;

      System.out.printf(
          "STATION: Advanced %d ticks. Busy time reduced from %d to %d.%n",
          ticksAdvanced, busyTimeRemaining, newBusyTime);

      if (newBusyTime == 0) {
        System.out.printf("STATION: Processing for material %s finished.%n", currentMaterial);
      }

      setBusyTimeRemaining(newBusyTime);
    }
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
    queue.getQueue().clear();
  }

  // Delegate methods

  protected abstract long getProcessingTime();
}
