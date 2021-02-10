package com.assemblyrobot.simulator.core.metrics;

import lombok.NonNull;

public class MetricsCollectorTypeNotRegisteredError extends Error {
  public MetricsCollectorTypeNotRegisteredError(@NonNull String className) {
    super("Metrics collector class %s not registered.".formatted(className));
  }
}
