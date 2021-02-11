package com.assemblyrobot.simulator.core.metrics.errors;

import lombok.NonNull;

public class DuplicateMetricsCollectorRegistrationError extends Error {
  public DuplicateMetricsCollectorRegistrationError(@NonNull String className) {
    super(
        "A metrics collector for class %s is already registered; duplicate registrations are not permitted."
            .formatted(className));
  }
}
