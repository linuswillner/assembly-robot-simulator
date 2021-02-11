package com.assemblyrobot.simulator.core.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EventTest {

  @Test
  void testToString() {
    assertEquals(
        "Event(executionTime=0, type=ARRIVAL)", new Event(0, EventType.ARRIVAL).toString());
  }

  @Test
  void getExecutionTime() {
    assertEquals(0, new Event(0, EventType.ARRIVAL).getExecutionTime());
  }

  @Test
  void getType() {
    assertEquals(EventType.ARRIVAL, new Event(0, EventType.ARRIVAL).getType());
  }
}
