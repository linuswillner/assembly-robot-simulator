package com.assemblyrobot.simulator.core.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/** Generic simulator event class. Takes a one-time-programmable execution time and event type. */
@ToString
@RequiredArgsConstructor
public class Event implements Comparable<Event> {
  @Getter private final long executionTime;
  @Getter @NonNull private final EventType type;

  @Override
  public int compareTo(@NonNull Event comparingEvent) {
    return Long.compare(executionTime, comparingEvent.getExecutionTime());
  }
}
