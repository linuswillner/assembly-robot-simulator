package com.assemblyrobot.simulator.core.metrics;

import com.assemblyrobot.simulator.core.metrics.errors.MetricsCollectorTypeNotRegisteredError;
import lombok.NonNull;

public enum MetricsCollectorType {
  ENGINE,
  STAGE,
  STAGE_CONTROLLER,
  STATION,
  MATERIAL;

  /**
   * Determines the appropriate metrics collector type based on a name (= organisational identifier).
   *
   * @param className Organisational identifier in the form of a class name.
   * @return {@link MetricsCollectorType}
   * @throws MetricsCollectorTypeNotRegisteredError If no such organisational identifier exists.
   */

  public static MetricsCollectorType getByClass(@NonNull String className) throws MetricsCollectorTypeNotRegisteredError{
    return switch (className) {
      case "com.assemblyrobot.simulator.core.Engine" -> ENGINE;
      case "com.assemblyrobot.simulator.system.components.Stage" -> STAGE;
      case "com.assemblyrobot.simulator.system.components.Station" -> STATION;
      case "com.assemblyrobot.simulator.system.components.StageController" -> STAGE_CONTROLLER;
      case "com.assemblyrobot.simulator.system.metricscollectors.MaterialMetricsCollector" -> MATERIAL;
      default -> throw new MetricsCollectorTypeNotRegisteredError(className);
    };
  }
}
