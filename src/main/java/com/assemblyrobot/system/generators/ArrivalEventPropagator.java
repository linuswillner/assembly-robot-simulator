package com.assemblyrobot.system.generators;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.EventType;
import com.assemblyrobot.simulator.core.generators.ArrivalEventIntervalGenerator;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
public class ArrivalEventPropagator {
  private final EventQueue eventQueue;
  private Event nextEvent = new Event(0, EventType.ARRIVAL);

  public void feedNext() {
    System.out.printf("ARRIVAL_EVENT_GENERATOR: Scheduling next arrival event %s.%n", nextEvent);
    eventQueue.schedule(nextEvent);

    // Schedule new next event
    val nextArrivalTime =
        Clock.getInstance().getCurrentTick()
            + ArrivalEventIntervalGenerator.getInstance().nextLong();
    nextEvent = new Event(nextArrivalTime, EventType.ARRIVAL);
    System.out.printf("ARRIVAL_EVENT_GENERATOR: Scheduling complete. Tee'd next arrival event %s.%n", nextEvent);
  }
}
