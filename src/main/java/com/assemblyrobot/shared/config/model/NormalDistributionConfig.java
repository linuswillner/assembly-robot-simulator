package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.Setter;

public class NormalDistributionConfig {
  @Getter @Setter private int mean;
  @Getter @Setter private int variance;
}
