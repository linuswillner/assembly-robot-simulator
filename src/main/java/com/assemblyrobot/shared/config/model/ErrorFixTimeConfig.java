package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.Setter;

public class ErrorFixTimeConfig {
  @Getter @Setter private int fittingFixTime;
  @Getter @Setter private int boltingFixTime;
  @Getter @Setter private int rivetingFixTime;
  @Getter @Setter private int weldingFixTime;
  @Getter @Setter private int returningFixTime;
}
