package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.Setter;

public class ErrorOccurrenceConfig {
  @Getter @Setter private double typeDistributionMean;
  @Getter @Setter private int errorThreshold;
}
