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

// TODO: Log4J
public abstract class Engine {
  @Getter(AccessLevel.PROTECTED) private final EventQueue eventQueue = new EventQueue();
  @Getter(AccessLevel.PROTECTED) private final ArrayList<Station> stations = new ArrayList<>(); // TODO: Priority queue based on which station is free
  private final EngineController engineController = new EngineController();
  private final Clock clock = Clock.getInstance();

  @Getter
  @Setter(AccessLevel.PRIVATE)
  private boolean isRunning = false;

  // Runner methods

  public void start() throws InterruptedException {
    System.out.println("ENGINE: Starting simulation.");

    System.out.println("ENGINE: Running initialisation routines.");
    init();

    System.out.println("ENGINE: Initialisation routines complete. Starting.");
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
    // Perform B events

    System.out.println("ENGINE: Performing B events.");
    var nextEvent = eventQueue.peekNext();

    while(nextEvent.getExecutionTime() == clock.getCurrentTick()) {
      nextEvent = eventQueue.pop();

      System.out.printf("ENGINE: Next event: %s%n", nextEvent);

      switch (nextEvent.getType()) {
        case ARRIVAL -> onArrival();
        case DEPARTURE -> onDeparture();
      }
    }

    // Tell points to check for C events
    System.out.println("ENGINE: Attempting to perform C events.");
    stations.forEach(Station::poll);

    // Advance clock
    val ticksToAdvance = eventQueue.peekNext().getExecutionTime() - clock.getCurrentTick();

    System.out.printf("ENGINE: Advancing clock by %d ticks.%n", ticksToAdvance);
    Thread.sleep(ticksToAdvance * 1000);
    clock.advanceTick(ticksToAdvance);
    System.out.printf("ENGINE: Clock tick is now %d.%n", clock.getCurrentTick());
  }

  // Delegate methods

  protected abstract void init();

  protected abstract void onArrival();

  protected abstract void onDeparture();
}
