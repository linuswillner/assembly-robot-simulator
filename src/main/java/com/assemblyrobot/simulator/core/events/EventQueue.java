package com.assemblyrobot.simulator.core.events;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventQueue {
  @Getter private final PriorityQueue<Event> queue = new PriorityQueue<>();
  private static final Logger logger = LogManager.getLogger();

  public void schedule(Event event) {
    queue.add(event);
  }

  public Event peekNext() {
    return queue.peek();
  }

  public Event pop() {
    return queue.poll();
  }

  public void dump() {
    val orderedQueue =
        queue.stream()
            .sorted(Comparator.comparingLong(Event::getExecutionTime))
            .collect(Collectors.toList());

    orderedQueue.forEach(logger::debug);
  }
}
