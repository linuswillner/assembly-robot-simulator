package com.assemblyrobot.simulator.core.events;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class EventQueueTest {
  private final EventQueue eventQueue = new EventQueue();

  @AfterEach
  void afterEach() {
    eventQueue.flush();
  }

  @Test
  @DisplayName("schedule(): Correctly schedules an event")
  void schedule() {
    val event = new Event(0, EventType.ARRIVAL);
    eventQueue.schedule(event);
    assertEquals(event, eventQueue.peekNext());
  }

  @Test
  @DisplayName("pop(): Correctly removes event from queue")
  void pop() {
    eventQueue.schedule(new Event(0, EventType.DEPARTURE));
    eventQueue.pop();
    assertNull(eventQueue.peekNext());
  }

  @Test
  void dump() {
    Event[] events = {
        new Event(0, EventType.ARRIVAL),
        new Event(1, EventType.DEPARTURE)
    };

    Arrays.stream(events).forEach(eventQueue::schedule);

    // Calling print() here as a faux call because print() is just a wrapper for dump(), and we're
    // actually going to be inspecting the contents of dump()
    eventQueue.print();

    assertArrayEquals(eventQueue.dump().toArray(), events);
  }
}
