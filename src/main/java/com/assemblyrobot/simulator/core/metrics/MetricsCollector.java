package com.assemblyrobot.simulator.core.metrics;

import java.util.HashMap;

public abstract class MetricsCollector {
  public MetricsCollector() {

  }

  public HashMap<String, Double> collect() {
    return null;
  }

  public enum Type {
    ENGINE,
    STAGE,
    STATION;

    public static Type getByClass(String className) {
      return switch (className) {
        case "com.assemblyrobot.simulator.core.Engine" -> ENGINE;
        case "com.assemblyrobot.simulator.system.components.Stage" -> STAGE;
        case "com.assemblyrobot.simulator.system.components.Station" -> STATION;
        default -> throw new MetricsCollectorTypeNotRegisteredError(className);
      };
    }
  }

  private static class MetricsCollectorTypeNotRegisteredError extends Error {
    public MetricsCollectorTypeNotRegisteredError(String className) {
      super("Metrics collector class %s not registered.".formatted(className));
    }
  }
}
