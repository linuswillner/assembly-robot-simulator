package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class ApplicationConfig {
  @Getter @Setter private NormalDistributionConfig arrivalIntervalParams;
  @Getter @Setter private NormalDistributionConfig assemblyTimeParams;
}
