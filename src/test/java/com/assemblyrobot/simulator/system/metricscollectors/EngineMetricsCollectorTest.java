package com.assemblyrobot.simulator.system.metricscollectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.metrics.CentralMetricsCollector;
import com.assemblyrobot.simulator.system.SimulatorEngine;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class EngineMetricsCollectorTest {

  @BeforeAll
  void beforeAll() {
    // Intentionally unused because otherwise the object will get GC'd and unregisters the metrics
    // collector
    val engine = new SimulatorEngine();
  }

  @Test
  @DisplayName("onTickAdvance(): Correctly observes tick advances")
  void onTickAdvance() {
    Clock.getInstance().nextTick();
    assertEquals(
        1,
        CentralMetricsCollector.getCollectors()
            .get("EngineMetricsCollector")
            .getMetric("total_simulation_time"));
  }

  @Test
  @DisplayName("onTickReset(): Correctly observes tick resets")
  void onTickReset() {
    Clock.getInstance().reset();
    assertEquals(
        0,
        CentralMetricsCollector.getCollectors()
            .get("EngineMetricsCollector")
            .getMetric("total_simulation_time"));
  }
}