package com.assemblyrobot.simulator.system.metricscollectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assemblyrobot.simulator.core.Engine;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.TransferEvent;
import com.assemblyrobot.simulator.core.metrics.CentralMetricsCollector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class EngineMetricsCollectorTest {
  private static class DummyEngine extends Engine {
    @Override
    protected void init() {}

    @Override
    protected void onArrival(Event event) {}

    @Override
    protected void onTransfer(TransferEvent event) {}

    @Override
    protected void onDeparture(Event event) {}
  }

  // Intentionally unused because otherwise the object will get GC'd and unregisters the metrics
  // collector
  private DummyEngine dummyEngine;

  @BeforeAll
  void beforeAll() {
    dummyEngine = new DummyEngine();
  }

  @Test
  @DisplayName("onTickAdvance(): Correctly observes tick advances")
  void onTickAdvance() {
    Clock.getInstance().nextTick();
    assertEquals(
        1,
        CentralMetricsCollector.getInstance()
            .getCollectors()
            .get("EngineMetricsCollector")
            .getMetric("total_simulation_time"));
  }

  @Test
  @DisplayName("onTickReset(): Correctly observes tick resets")
  void onTickReset() {
    Clock.getInstance().reset();
    assertEquals(
        0,
        CentralMetricsCollector.getInstance()
            .getCollectors()
            .get("EngineMetricsCollector")
            .getMetric("total_simulation_time"));
  }
}
