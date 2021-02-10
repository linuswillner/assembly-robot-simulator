package com.assemblyrobot.simulator.system.components;

import com.assemblyrobot.simulator.core.clock.TickAdvanceListener;
import com.assemblyrobot.simulator.system.controllers.StageController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public abstract class Station extends TickAdvanceListener {
  @Getter private final StageController controller = new StageController();
  @Getter private final MaterialQueue queue = new MaterialQueue();
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private long busyTimeRemaining = 0;

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private Material currentMaterial = null;

  // Busy logic

  public boolean isBusy() {
    return busyTimeRemaining > 0;
  }

  private boolean canPull() {
    return !isBusy() && queue.getQueue().size() > 0;
  }

  // Queue operations

  public void addToQueue(Material material) {
    controller.registerIncomingMaterial(material);
    queue.add(material);
  }

  private Material pullFromQueue() {
    return queue.pop();
  }

  public void poll() {
    // Using a while loop on canPull() in case we somehow get events that resolve instantly
    while (canPull()) {
      val next = pullFromQueue();
      setCurrentMaterial(next);
      controller.registerMaterialProcessing(next.getId());
      val processingTime = getProcessingTime();

      setBusyTimeRemaining(processingTime);

      logger.trace("Starting processing of {}. Processing will continue for {} ticks.", next, processingTime);
    }
  }

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
    queue.getQueue().clear();
  }

  // Delegate methods

  protected abstract long getProcessingTime();
}
