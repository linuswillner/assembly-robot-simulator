package com.assemblyrobot.simulator.core.events;

import java.util.PriorityQueue;
import lombok.Getter;

public class EventQueue {
  @Getter private final PriorityQueue<Event> queue = new PriorityQueue<>();

  public void schedule(Event event) {
    queue.add(event);
  }

  public Event peekNext() {
    return queue.peek();
  }

  public Event pop() {
    return queue.poll();
  }
}
