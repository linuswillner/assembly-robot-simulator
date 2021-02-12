package com.assemblyrobot.simulator.core.metrics;

import static org.junit.jupiter.api.Assertions.*;

import com.assemblyrobot.simulator.core.metrics.errors.DuplicateMetricsCollectorRegistrationError;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class CentralMetricsCollectorTest {
  private final CentralMetricsCollector centralMetricsCollector =
      CentralMetricsCollector.getInstance();

  @BeforeAll
  void beforeAll() {
    CentralMetricsCollector.getInstance().dump();
  }

  @Test
  @DisplayName("registerMetricsCollector(): Throws an error if duplicate registrations occur")
  void registerMetricsCollector() {
    val collector =
        new MetricsCollector(getClass().getName(), "com.assemblyrobot.simulator.core.Engine");

    assertThrows(
        DuplicateMetricsCollectorRegistrationError.class,
        () -> centralMetricsCollector.registerMetricsCollector(getClass().getName(), collector));
  }
}
