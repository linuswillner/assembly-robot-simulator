package com.assemblyrobot.simulator.core;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.system.controllers.StageController;
import com.assemblyrobot.simulator.system.metricscollectors.EngineMetricsCollector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generic simulator engine. Used for running the simulation.
 */
public abstract class Engine {
  @Getter(AccessLevel.PROTECTED) private final EventQueue eventQueue = new EventQueue();
  @Getter(AccessLevel.PROTECTED) private final StageController  stageController = new StageController(eventQueue);
  private final Clock clock = Clock.getInstance();
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean isRunning = false;

  public Engine() {
    new EngineMetricsCollector(this); // Register engine metrics collector
  }

  // Runner methods

  /**
   * Starts the engine.
   * @throws InterruptedException If a Thread.sleep() operation is interrupted.
   */
  public void start() throws InterruptedException {
    logger.info("Starting simulation.");

    logger.info("Running initialisation routines.");
    init();

    logger.info("Initialisation routines complete. Starting event loop.");
    setRunning(true);

    while (isRunning()) {
      runCycle();
    }
  }

  /**
   * Stops the engine.
   */
  public void stop() {
    logger.warn("ENGINE: Stopping simulation.");
    setRunning(false);
  }

  /**
   * Runs one "CPU" cycle.
   * @throws InterruptedException If a Thread.sleep() operation is interrupted.
   */
  private void runCycle() throws InterruptedException {
    logger.trace("Beginning new cycle.");

    // Perform B events

    logger.trace("Performing B events.");
    var nextEvent = eventQueue.peekNext();

    while(nextEvent.getExecutionTime() == clock.getCurrentTick()) {
      logger.trace("Next event: {}", nextEvent);

      switch (nextEvent.getType()) {
        case ARRIVAL -> onArrival();
        case DEPARTURE -> onDeparture();
      }

      eventQueue.pop();
      nextEvent = eventQueue.peekNext();
    }

    // Tell points to check for C events
    logger.trace("Attempting to perform C events.");
    //stations.forEach(Station::poll);

    // Dump event queue for debug
    logger.trace("All events performed. Dumping future event queue.");
    System.out.println("---");
    eventQueue.print();
    System.out.println("---");
    logger.trace("Future event queue dumped.");

    // Advance clock
    val ticksToAdvance = eventQueue.peekNext().getExecutionTime() - clock.getCurrentTick();

    logger.trace("Advancing clock by {} ticks to tick {}.", ticksToAdvance, clock.getCurrentTick() + ticksToAdvance);
    Thread.sleep(ticksToAdvance * 1000);
    clock.advanceTick(ticksToAdvance);
    logger.trace("Clock tick is now {}.", clock.getCurrentTick());
  }

  // Delegate methods

  protected abstract void init();

  protected abstract void onArrival();

  protected abstract void onDeparture();
}
