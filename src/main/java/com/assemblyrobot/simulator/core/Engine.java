package com.assemblyrobot.simulator.core;

import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.simulator.core.events.Event;
import com.assemblyrobot.simulator.core.events.EventQueue;
import com.assemblyrobot.simulator.core.events.TransferEvent;
import com.assemblyrobot.simulator.system.components.StageController;
import com.assemblyrobot.simulator.system.metricscollectors.EngineMetricsCollector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
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
  private static final Logger logger = LogManager.getLogger();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean isRunning = false;

  @Setter private long stopTick = 0;

  public Engine() {
    new EngineMetricsCollector(this); // Register engine metrics collector
  }

  /**
   * Starts the Engine thread. Do not call this method manually; call the start() method instead.
   */
  @SneakyThrows
  @Override
  public void run() {
    startEngine();
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
    logger.warn("ENGINE: Stopping simulation.");
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

    // Tell points to check for C events
    logger.trace("Attempting to perform C events.");

    // Dump event queue for debug
    logger.trace("All events performed. Dumping future event queue.");
    System.out.println("---");
    eventQueue.print();
    System.out.println("---");
    logger.trace("Future event queue dumped.");

    // Advance clock
    val ticksToAdvance = eventQueue.peekNext().getExecutionTime() - clock.getCurrentTick();

    logger.trace(
        "Advancing clock by {} ticks to tick {}.",
        ticksToAdvance,
        clock.getCurrentTick() + ticksToAdvance);
    Thread.sleep(ticksToAdvance * 1000);
    clock.advanceTick(ticksToAdvance);
    logger.trace("Clock tick is now {}.", clock.getCurrentTick());
  }

  // Delegate methods

  protected abstract void init();

  protected abstract void onArrival(Event event);

  protected abstract void onTransfer(TransferEvent event);

  protected abstract void onDeparture(Event event);
}
