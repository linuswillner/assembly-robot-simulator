package com.assemblyrobot.simulator.core.metrics.errors;

import lombok.NonNull;

public class MetricsCollectorTypeNotRegisteredError extends Error {
  public MetricsCollectorTypeNotRegisteredError(@NonNull String className) {
    super("Metrics collector type class %s not registered.".formatted(className));
  }
}
