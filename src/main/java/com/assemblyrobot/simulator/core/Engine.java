package com.assemblyrobot.simulator.core;

import com.assemblyrobot.system.core.Station;
import com.assemblyrobot.simulator.core.clock.Clock;
import com.assemblyrobot.system.controllers.EngineController;
import com.assemblyrobot.simulator.core.events.EventQueue;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

// TODO: Implement Log4J
public abstract class Engine {
  @Getter(AccessLevel.PROTECTED) private final EventQueue eventQueue = new EventQueue();
  // TODO: Hand off this process to StageController once Stages are implemented, see spec for implementation details
  @Getter(AccessLevel.PROTECTED) private final ArrayList<Station> stations = new ArrayList<>();
  private final EngineController engineController = new EngineController(); // TODO: Not used for now
  private final Clock clock = Clock.getInstance();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean isRunning = false;

  // Runner methods

  public void start() throws InterruptedException {
    System.out.println("ENGINE: Starting simulation.");

    System.out.println("ENGINE: Running initialisation routines.");
    init();

    System.out.println("ENGINE: Initialisation routines complete. Starting event loop.");
    setRunning(true);

    while (isRunning()) {
      runCycle();
    }
  }

  public void stop() {
    System.out.println("ENGINE: Stopping simulation.");
    setRunning(false);
  }

  private void runCycle() throws InterruptedException {
    System.out.println("---\nENGINE: Beginning new cycle.");

    // Perform B events

    System.out.println("ENGINE: Performing B events.");
    var nextEvent = eventQueue.peekNext();

    while(nextEvent.getExecutionTime() == clock.getCurrentTick()) {
      System.out.printf("ENGINE: Next event: %s%n", nextEvent);

      switch (nextEvent.getType()) {
        case ARRIVAL -> onArrival();
        case DEPARTURE -> onDeparture();
      }

      eventQueue.pop();
      nextEvent = eventQueue.peekNext();
    }

    // Tell points to check for C events
    System.out.println("ENGINE: Attempting to perform C events.");
    stations.forEach(Station::poll);

    // Dump event queue for debug
    System.out.println("ENGINE: All events performed. Dumping future event queue.\n---");
    eventQueue.dump();
    System.out.println("---\nENGINE: Future event queue dumped.");

    // Advance clock
    val ticksToAdvance = eventQueue.peekNext().getExecutionTime() - clock.getCurrentTick();

    System.out.printf("ENGINE: Advancing clock by %d ticks to tick %d.%n", ticksToAdvance, clock.getCurrentTick() + ticksToAdvance);
    Thread.sleep(ticksToAdvance * 1000);
    clock.advanceTick(ticksToAdvance);
    System.out.printf("ENGINE: Clock tick is now %d.%n", clock.getCurrentTick());
  }

  // Delegate methods

  protected abstract void init();

  protected abstract void onArrival();

  protected abstract void onDeparture();
}
