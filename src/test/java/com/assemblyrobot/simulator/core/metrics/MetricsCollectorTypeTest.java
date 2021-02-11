package com.assemblyrobot.simulator.core.metrics;

import static org.junit.jupiter.api.Assertions.*;

import com.assemblyrobot.simulator.core.metrics.errors.MetricsCollectorTypeNotRegisteredError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MetricsCollectorTypeTest {
  private final MetricsCollectorType[] metricsTypes = {
    MetricsCollectorType.ENGINE, MetricsCollectorType.STAGE, MetricsCollectorType.STATION
  };

  private final String[] typeClasses = {
    "com.assemblyrobot.simulator.core.Engine",
    "com.assemblyrobot.simulator.system.components.Stage",
    "com.assemblyrobot.simulator.system.components.Station"
  };

  @Test
  @DisplayName("getByClass(): Returns the correct type for all known type classes")
  void getByClass() {
    for (int i = 0; i < metricsTypes.length; i++) {
      assertEquals(metricsTypes[i], MetricsCollectorType.getByClass(typeClasses[i]));
    }
  }

  @Test
  @DisplayName("getByClass(): Throws an error if type class is unknown")
  void getByClassThrows() {
    assertThrows(
        MetricsCollectorTypeNotRegisteredError.class,
        () -> MetricsCollectorType.getByClass("test"));
  }
}
