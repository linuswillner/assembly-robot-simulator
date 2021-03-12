package com.assemblyrobot.ui.models;

import com.assemblyrobot.shared.config.model.ErrorOccurrenceConfig;
import lombok.Getter;

/**
 * Visualisation transformer class for {@link
 * com.assemblyrobot.shared.config.model.ErrorOccurrenceConfig}s.
 */
public class ErrorOccurrenceVisualization {
  @Getter private final String setting;
  @Getter private final double typeDistributionMean;
  @Getter private final int errorThreshold;

  public ErrorOccurrenceVisualization(String setting, ErrorOccurrenceConfig config) {
    this.setting = setting;
    typeDistributionMean = config.getTypeDistributionMean();
    errorThreshold = config.getErrorThreshold();
  }
}
