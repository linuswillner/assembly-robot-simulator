package com.assemblyrobot;

import com.assemblyrobot.system.engines.AssemblyEngine;
import lombok.SneakyThrows;

public class PrototypeTest {
  private static final AssemblyEngine ASSEMBLY_ENGINE = new AssemblyEngine();

  @SneakyThrows
  public static void main(String[] args) {
    ASSEMBLY_ENGINE.start();
  }
}
