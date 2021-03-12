package com.assemblyrobot.simulator.core;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.TransferEvent;
import com.assemblyrobot.simulator.system.components.StageController;
import com.assemblyrobot.simulator.system.metricscollectors.EngineMetricsCollector;
import com.assemblyrobot.ui.controllers.StationViewerController;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Generic simulator engine. Used for running the simulation. */
public abstract class Engine extends Thread {
  @Getter(AccessLevel.PROTECTED)
  private final EventQueue eventQueue = new EventQueue();

  @Getter(AccessLevel.PROTECTED)
  private final StageController stageController = new StageController(eventQueue);

  private final Clock clock = Clock.getInstance();

  // Runtime parameters

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean isRunning = false;

  @Setter
  private StationViewerController
      stationViewerController; // Required for refresh logic before tick advancements

  @Setter private boolean isPause;
  @Setter private boolean canProceed;
  @Setter private double speedMultiplier = 0;
  @Setter private long stopTick = 0;

  private static final Logger logger = LogManager.getLogger();

  public Engine() {
    new EngineMetricsCollector(this); // Register engine metrics collector
  }

  /**
   * Starts the Engine thread. Do not call this method manually; call the start() method instead.
   */
  @Override
  public void run() {
    try {
      startEngine();
    } catch (InterruptedException e) {
      // Silently swallow InterruptedExceptions as this indicates stopping of simulation
    }
  }

  /**
   * Starts the engine.
   *
   * @throws InterruptedException If a Thread.sleep() operation is interrupted.
   */
  private void startEngine() throws InterruptedException {
    logger.info("Starting simulation.");

    logger.info("Running initialisation routines.");
    init();

    logger.info("Initialisation routines complete. Starting event loop.");
    setRunning(true);

    // Run while stopTick is above 0 (=> not default infinite) and below the set limit,
    // or per default until told to stop
    while (stopTick != 0 ? clock.getCurrentTick() <= stopTick : isRunning()) {
      runCycle();
    }
  }

  /** Stops the engine. */
  public void endRun() {
    logger.warn("Stopping simulation.");
    setRunning(false);
  }

  /**
   * Runs one "CPU" cycle.
   *
   * @throws InterruptedException If a Thread.sleep() operation is interrupted.
   */
  private void runCycle() throws InterruptedException {
    logger.trace("Beginning new cycle.");

    // Perform B events

    logger.trace("Performing B events.");
    var nextEvent = eventQueue.peekNext();

    while (nextEvent.getExecutionTime() == clock.getCurrentTick()) {
      logger.trace("Next event: {}", nextEvent);

      switch (nextEvent.getType()) {
        case ARRIVAL -> onArrival(nextEvent);
        case TRANSFER -> onTransfer((TransferEvent) nextEvent);
        case DEPARTURE -> onDeparture(nextEvent);
        // Not handling the PROCESSING_COMPLETE event as we need to do nothing to it
      }

      eventQueue.pop();
      nextEvent = eventQueue.peekNext();
    }

    // Tell stations to check for C events
    logger.trace("Attempting to perform C events.");

    // Dump event queue for debug
    logger.trace("All events performed. Dumping future event queue.");
    System.out.println("---");
    eventQueue.print();
    System.out.println("---");
    logger.trace("Future event queue dumped.");

    // Test UI button status
    if (isPause) logger.trace("Simulation paused.");

    // Wait for user to allow the simulator to proceed
    while (isPause && !canProceed) {
      //noinspection BusyWait
      Thread.sleep(1);
    }

    // Reset for the next cycle
    canProceed = false;

    // Refresh station viewer here before we advance to the next event to indicate the state of the
    // simulator as it currently stands
    stationViewerController.refreshStationViewer();

    // Advance clock
    val ticksToAdvance = eventQueue.peekNext().getExecutionTime() - clock.getCurrentTick();

    logger.trace(
        "Advancing clock by {} ticks to tick {}.",
        ticksToAdvance,
        clock.getCurrentTick() + ticksToAdvance);

    // Calculate appropriate sleep time based on user-defined speed multiplier
    if (speedMultiplier < 0) {
      // If slowing down
      Thread.sleep(ticksToAdvance * (Math.round(speedMultiplier / -1)) * 1000);
    } else if (speedMultiplier > 0) {
      // If speeding up
      Thread.sleep(ticksToAdvance * (1000 / Math.round(speedMultiplier)));
    } else {
      // If staying at default
      Thread.sleep(ticksToAdvance * 1000);
    }

    clock.advanceTick(ticksToAdvance);
    logger.trace("Clock tick is now {}.", clock.getCurrentTick());
  }

  // Delegate methods

  protected abstract void init();

  protected abstract void onArrival(Event event);

  protected abstract void onTransfer(TransferEvent event);

  protected abstract void onDeparture(Event event);
}
