package com.assemblyrobot.simulator.core.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class Event implements Comparable<Event> {
  @Getter private final long executionTime;
  @Getter private final EventType type;

  @Override
  public int compareTo(Event comparingEvent) {
    return Long.compare(executionTime, comparingEvent.getExecutionTime());
  }
}
