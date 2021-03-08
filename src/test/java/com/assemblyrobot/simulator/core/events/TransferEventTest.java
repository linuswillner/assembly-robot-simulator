package com.assemblyrobot.simulator.core.events;

import static org.junit.jupiter.api.Assertions.*;

import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.shared.constants.StageID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferEventTest {

  @Test
  @DisplayName("toString(): Returns information with expansions")
  void testToString() {
    assertEquals(
        "TransferEvent(executionTime=0, type=TRANSFER, destination=ERROR_CHECK, error=FITTING)",
        new TransferEvent(0, EventType.TRANSFER, StageID.ERROR_CHECK, ErrorType.FITTING).toString());
  }
}
