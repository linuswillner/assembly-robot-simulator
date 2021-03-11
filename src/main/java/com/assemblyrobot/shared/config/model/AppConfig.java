package com.assemblyrobot.shared.config.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class AppConfig {
  @Getter @Setter private List<String> requiredEnv;
  @Getter @Setter private int windowWidth;
  @Getter @Setter private int windowHeight;
  @Getter @Setter private int popupWindowWidth;
  @Getter @Setter private int popupWindowHeight;
}
