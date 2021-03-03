package com.assemblyrobot;

import com.assemblyrobot.simulator.system.SimulatorEngine;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class PrototypeTest {
  private static final SimulatorEngine ASSEMBLY_ENGINE = new SimulatorEngine();

  @SneakyThrows
  public static void main(String[] args) {
    Configurator.setRootLevel(Level.TRACE);
    ASSEMBLY_ENGINE.start();
  }
}
