package com.assemblyrobot;

import com.assemblyrobot.system.engines.AssemblyEngine;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

public class PrototypeTest {
  private static final AssemblyEngine ASSEMBLY_ENGINE = new AssemblyEngine();

  @SneakyThrows
  public static void main(String[] args) {
    Configurator.setRootLevel(Level.TRACE);
    ASSEMBLY_ENGINE.start();
  }
}
