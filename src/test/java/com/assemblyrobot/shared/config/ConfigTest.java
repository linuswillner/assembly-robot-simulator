package com.assemblyrobot.shared.config;

import static org.hamcrest.CoreMatchers.instanceOf;

import com.assemblyrobot.shared.config.model.ApplicationConfig;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigTest {

  @Test
  @DisplayName("getConfig(): Loads and returns config correctly")
  void getConfig() {
    val config = Config.getConfig();
    MatcherAssert.assertThat(config, instanceOf(ApplicationConfig.class));
  }
}