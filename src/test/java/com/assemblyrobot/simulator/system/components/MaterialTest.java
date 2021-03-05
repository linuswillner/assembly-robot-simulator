package com.assemblyrobot.simulator.system.components;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MaterialTest {

  private Material material = new Material();

  @BeforeEach
  void beforeEach(){
    material.reset();
    material.resetId();
  }

  @Test
  @DisplayName("reset(): Resets all data to 0.")
  void reset() {
    assertAll("data reset",
        () -> assertEquals(0, material.getQueueStartTime()),
        () -> assertEquals(0, material.getQueueEndTime()),
        () -> assertEquals(0, material.getProcessingStartTime()),
        () -> assertEquals(0, material.getProcessingEndTime())
    );
  }

  @Test
  @DisplayName("getTotalPassthroughTime(): Returns total passthrough time correctly.")
  void getTotalPassthroughTime() {
    material.setQueueStartTime(1);
    material.setProcessingEndTime(2);
    assertEquals(1, material.getTotalPassthroughTime());
  }

  @Test
  @DisplayName("setQueueStartTime(): Sets queueStartTime correctly.")
  void setQueueStartTime() {
    material.setQueueStartTime(1);
    assertEquals(1, material.getQueueStartTime());
  }

  @Test
  @DisplayName("setQueueEndTime(): Sets queueEndTime correctly.")
  void setQueueEndTime() {
    material.setQueueEndTime(1);
    assertEquals(1, material.getQueueEndTime());
  }

  @Test
  @DisplayName("setProcessingStartTime(): Sets processingStartTime correctly.")
  void setProcessingStartTime() {
    material.setProcessingStartTime(1);
    assertEquals(1, material.getProcessingStartTime());
  }

  @Test
  @DisplayName("setProcessingEndTime(): Sets processingEndTime correctly.")
  void setProcessingEndTime() {
    material.setProcessingEndTime(1);
    assertEquals(1, material.getProcessingEndTime());
  }

  @Test
  @DisplayName("getId(): Returns material id correctly.")
  void getId() {
    assertEquals(1, material.getId());
  }

  @Test
  @DisplayName("getQueueStartTime(): Returns queueStartTime correctly.")
  void getQueueStartTime() {
    assertEquals(0, material.getQueueStartTime());
  }

  @Test
  @DisplayName("getQueueEndTime(): Returns queueEndTime correctly.")
  void getQueueEndTime() {
    assertEquals(0, material.getQueueEndTime());
  }

  @Test
  @DisplayName("getProcessingStartTime(): Returns processingStartTime correctly.")
  void getProcessingStartTime() {
    assertEquals(0, material.getProcessingStartTime());
  }

  @Test
  @DisplayName("getProcessingEndTime(): Returns processingEndTime correctly.")
  void getProcessingEndTime() {
    assertEquals(0, material.getProcessingEndTime());
  }
}