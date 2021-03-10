package com.assemblyrobot.ui.controllers;

import com.assemblyrobot.simulator.system.SimulatorEngine;
import lombok.Getter;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class OverviewController {

  @Getter private static final SimulatorEngine engine = new SimulatorEngine();
  private static final Logger logger = LogManager.getLogger();

  public void startEngine() {
    Configurator.setRootLevel(Level.TRACE);
    engine.start();
  }

  public void setPause(boolean isPause) {
    engine.setPause(isPause);
  }

  public void setCanProceed(boolean canProceed){
    engine.setCanProceed(canProceed);
  }

  public void takeStep() {
    engine.setCanProceed(true);
    logger.trace("Taking a step forward.");
  }

  public void setSpeed(double value){
    engine.setSpeedMultiplier(value);
  }
}
