package com.assemblyrobot.simulator.core.events;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
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
   * Dumps the event queue, in ascending order based on execution time.
   *
   * @return {@link List}&lt;{@link Event}&gt;
   */
  public List<Event> dump() {
    return queue.stream()
        .sorted(Comparator.comparingLong(Event::getExecutionTime))
        .collect(Collectors.toList());
  }

  /** Prints the event queue to the terminal. */
  public void print() {
    dump().forEach(logger::debug);
  }

  /** Empties the event queue. */
  public void flush() {
    queue.clear();
  }
}
