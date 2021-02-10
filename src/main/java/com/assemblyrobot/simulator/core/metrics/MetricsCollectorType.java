package com.assemblyrobot.simulator.core.metrics;

import lombok.NonNull;

public enum MetricsCollectorType {
  ENGINE,
  STAGE,
  STATION;

  public static MetricsCollectorType getByClass(@NonNull String className) {
    return switch (className) {
      case "com.assemblyrobot.simulator.core.Engine" -> ENGINE;
      case "com.assemblyrobot.simulator.system.components.Stage" -> STAGE;
      case "com.assemblyrobot.simulator.system.components.Station" -> STATION;
      default -> throw new MetricsCollectorTypeNotRegisteredError(className);
    };
  }
}
