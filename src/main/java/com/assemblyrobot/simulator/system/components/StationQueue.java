package com.assemblyrobot.simulator.system.components;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class StationQueue {

  private final PriorityQueue<Station> queue = new PriorityQueue<>();

  public void add(Station station) {
    queue.add(station);
  }

  public Station peek() {
    return queue.peek();
  }

  public List<Station> getQueue() {
    return new ArrayList<>(queue);
  }


}
