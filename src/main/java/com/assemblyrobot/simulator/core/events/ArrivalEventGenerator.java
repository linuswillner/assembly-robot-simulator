package com.assemblyrobot.simulator.core.events;

import com.assemblyrobot.simulator.core.clock.Clock;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class ArrivalEventGenerator {
  private final EventQueue eventQueue;
  private Event nextEvent = new Event(0, EventType.ARRIVAL);

  public void feedNext() {
    System.out.printf("ARRIVAL_EVENT_GENERATOR: Scheduling next arrival event %s.%n", nextEvent);
    eventQueue.schedule(nextEvent);

    // Schedule new next event
    val nextArrivalTime = Clock.getInstance().getCurrentTick() + 20; // TODO: Deconstantify
    nextEvent = new Event(nextArrivalTime, EventType.ARRIVAL);
    System.out.printf("ARRIVAL_EVENT_GENERATOR: Scheduled new next arrival event %s.%n", nextEvent);
  }
}
