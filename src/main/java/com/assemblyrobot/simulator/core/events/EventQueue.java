package com.assemblyrobot.simulator.core.events;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Generic event queue. */
public class EventQueue {
  @Getter private final PriorityQueue<Event> queue = new PriorityQueue<>();
  private static final Logger logger = LogManager.getLogger();

  /**
   * Schedules an {@link Event} for future execution.
   *
   * @param event The event to schedule.
   */
  public void schedule(@NonNull Event event) {
    queue.add(event);
  }

  /**
   * Returns the next {@link Event} to be executed.
   *
   * @return {@link Event}
   */
  public Event peekNext() {
    return queue.peek();
  }

  /**
   * Returns the next {@link Event} to be executed, simultaneously removing it from the queue.
   *
   * @return {@link Event}
   */
  public Event pop() {
    return queue.poll();
  }

  /**
   * Utility function to dump the entire event queue to the log, ordered in ascending order by
   * execution time.
   */
  public void dump() {
    val orderedQueue =
        queue.stream()
            .sorted(Comparator.comparingLong(Event::getExecutionTime))
            .collect(Collectors.toList());

    orderedQueue.forEach(logger::debug);
  }
}
