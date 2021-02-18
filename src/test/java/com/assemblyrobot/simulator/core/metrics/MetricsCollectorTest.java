package com.assemblyrobot.simulator.core.metrics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class MetricsCollectorTest {
  // Faking that we're coming from the engine so that we don't get errors as a result of that
  private MetricsCollector metricsCollector;
  private static final String METRIC_NAME = "test_metric";

  @BeforeAll
  void beforeAll() {
    metricsCollector =
        new MetricsCollector(getClass().getSimpleName(), "com.assemblyrobot.simulator.core.Engine");
  }

  @AfterEach
  void afterEach() {
    metricsCollector.getMetrics().clear();
  }

  @Test
  @DisplayName("getMetric(): Correctly returns a set metric")
  void getMetric() {
    metricsCollector.putMetric(METRIC_NAME, 0.0);
    assertEquals(metricsCollector.getMetric(METRIC_NAME), 0);
  }

  @Test
  @DisplayName("getMetric(): Throws an error on unset metric")
  void getMetricThrows() {
    assertThrows(NullPointerException.class, () -> metricsCollector.getMetric(METRIC_NAME));
  }

  @Test
  @DisplayName("getMetric(): Returns a default value if no metric set")
  void getMetricWithDefault() {
    assertEquals(1.0, metricsCollector.getMetric(METRIC_NAME, 1.0));
  }

  @Test
  @DisplayName("getType(): Returns the correct type")
  void getType() {
    assertEquals(MetricsCollectorType.ENGINE, metricsCollector.getType());
  }
}
