package com.assemblyrobot;

import com.assemblyrobot.simulator.system.engines.MainEngine;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class PrototypeTest {
  private static final MainEngine ASSEMBLY_ENGINE = new MainEngine();

  @SneakyThrows
  public static void main(String[] args) {
    Configurator.setRootLevel(Level.TRACE);
    ASSEMBLY_ENGINE.start();
  }
}
