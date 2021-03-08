package com.assemblyrobot.simulator.system.metricscollectors;

import static org.junit.jupiter.api.Assertions.*;

import com.assemblyrobot.shared.constants.StageID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaterialMetricsCollectorTest {

  //TODO: Can't register duplicate metric collectors, possible workaround?
  MaterialMetricsCollector metricsCollector =
      new MaterialMetricsCollector(StageID.ASSEMBLY,"Assembly", 1);

  @BeforeEach
  void beforeEach() {
    metricsCollector.setQueueEndTime(0);
    metricsCollector.setQueueEndTime(0);
    metricsCollector.setProcessingEndTime(0);
    metricsCollector.setProcessingEndTime(0);
  }

  @Test
  void getQueueDuration() {
    metricsCollector.setQueueStartTime(1);
    metricsCollector.setQueueEndTime(2);
    assertEquals(1, metricsCollector.getQueueDuration());
  }

  @Test
  void getProcessingDuration() {
    metricsCollector.setProcessingStartTime(1);
    metricsCollector.setProcessingEndTime(2);
    assertEquals(1, metricsCollector.getProcessingDuration());
  }

  @Test
  void getTotalPassthroughTime() {
    metricsCollector.setQueueStartTime(1);
    metricsCollector.setProcessingEndTime(2);
    assertEquals(1, metricsCollector.getTotalPassthroughTime());
  }

  @Test
  void updateMetrics() {}

  @Test
  @DisplayName("setQueueStartTime(): Sets queueStartTime correctly.")
  void setQueueStartTime() {
    metricsCollector.setQueueStartTime(1);
    assertEquals(1, metricsCollector.getQueueStartTime());
  }

  @Test
  @DisplayName("setQueueEndTime(): Sets queueEndTime correctly.")
  void setQueueEndTime() {
    metricsCollector.setQueueEndTime(1);
    assertEquals(1, metricsCollector.getQueueEndTime());
  }

  @Test
  @DisplayName("setProcessingStartTime(): Sets processingStartTime correctly.")
  void setProcessingStartTime() {
    metricsCollector.setProcessingStartTime(1);
    assertEquals(1, metricsCollector.getProcessingStartTime());
  }

  @Test
  @DisplayName("setProcessingEndTime(): Sets processingEndTime correctly.")
  void setProcessingEndTime() {
    metricsCollector.setProcessingEndTime(1);
    assertEquals(1, metricsCollector.getProcessingEndTime());
  }

  @Test
  @DisplayName("getQueueStartTime(): Returns queueStartTime correctly.")
  void getQueueStartTime() {
    assertEquals(0, metricsCollector.getQueueStartTime());
  }

  @Test
  @DisplayName("getQueueEndTime(): Returns queueEndTime correctly.")
  void getQueueEndTime() {
    assertEquals(0, metricsCollector.getQueueEndTime());
  }

  @Test
  @DisplayName("getProcessingStartTime(): Returns processingStartTime correctly.")
  void getProcessingStartTime() {
    assertEquals(0, metricsCollector.getProcessingStartTime());
  }

  @Test
  @DisplayName("getProcessingEndTime(): Returns processingEndTime correctly.")
  void getProcessingEndTime() {
    assertEquals(0, metricsCollector.getProcessingEndTime());
  }
}
