package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.controllers.StationController;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public abstract class Station extends TickAdvanceListener implements BiDirectionalQueueable{



// This code is the original way of handling a station's tick advances. Left here in case there's a need to refer to it.
/*
  // Tick advance listener methods

  @Override
  protected void onTickAdvance(long ticksAdvanced) {
    if (isBusy()) {
      val newBusyTime = busyTimeRemaining - ticksAdvanced;

      logger.trace("Advanced {} ticks. Busy time reduced from {} to {}.", ticksAdvanced, busyTimeRemaining, newBusyTime);

      if (newBusyTime == 0) {
        logger.trace("Processing for material {} finished.", currentMaterial);
      }

      setBusyTimeRemaining(newBusyTime);
    }
  }

  @Override
  protected void onTickReset() {
    busyTimeRemaining = 0;
    materialQueue.getQueue().clear();
  }
*/

  // Delegate methods
  protected abstract long getProcessingTime();
}
