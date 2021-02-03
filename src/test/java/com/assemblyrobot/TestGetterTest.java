package com.assemblyrobot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestGetterTest {

  @Test
  void getHelloWorld() {
    assertEquals(TestGetter.getHelloWorld(), "Hello World!", "Incorrect capitalisation");
  }
}
