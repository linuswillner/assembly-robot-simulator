package com.assemblyrobot.simulator.system.components;

import java.util.LinkedList;
import lombok.Getter;

public class MaterialQueue {
  @Getter private final LinkedList<Material> queue = new LinkedList<>();

  public void add(Material material) {
    queue.add(material);
  }

  public Material peekNext() {
    return queue.peek();
  }

  public Material pop() {
    return queue.poll();
  }
}
