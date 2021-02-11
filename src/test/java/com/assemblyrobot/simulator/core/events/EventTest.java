package com.assemblyrobot.simulator.core.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {
  @Test
  @DisplayName("toString(): Returns correctly formatted string")
  void testToString() {
    assertEquals(
        "Event(executionTime=0, type=ARRIVAL)", new Event(0, EventType.ARRIVAL).toString());
  }

  @Test
  @DisplayName("getExecutionTime(): Returns correct execution time")
  void getExecutionTime() {
    assertEquals(0, new Event(0, EventType.ARRIVAL).getExecutionTime());
  }

  @Test
  @DisplayName("getType(): Returns correct type")
  void getType() {
    assertEquals(EventType.ARRIVAL, new Event(0, EventType.ARRIVAL).getType());
  }
}
