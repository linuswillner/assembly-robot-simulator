package com.assemblyrobot.simulator.system.components;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Generic station list/queue, ordered by vacancy (I.e. the simulator makes an assumption of
 * automatically prioritising empty stations over full ones, {@link Material}s get no choice over
 * this).
 */
public class StationQueue {
  private final PriorityQueue<Station> queue = new PriorityQueue<>();

  /**
   * Adds a station to the queue.
   *
   * @param station {@link Station}
   */
  public void add(Station station) {
    queue.add(station);
  }

  /**
   * Returns the next vacant {@link Station}.
   *
   * @return First vacant {@link Station}.
   */
  public Station peek() {
    return queue.peek();
  }

  /**
   * Returns a {@link List} of all {@link Station}s in this queue.
   *
   * @return {@link List}&lt;{@link Station}&gt;
   */
  public List<Station> getAll() {
    return new ArrayList<>(queue);
  }

  /** Dumps all stations from the queue. Used for resets between simulator runs. */
  public void dump() {
    queue.clear();
  }
}
