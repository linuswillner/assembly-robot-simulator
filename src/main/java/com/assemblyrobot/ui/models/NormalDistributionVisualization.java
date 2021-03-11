package com.assemblyrobot.ui.models;

import com.assemblyrobot.shared.config.model.NormalDistributionConfig;
import lombok.Getter;

public class NormalDistributionVisualization {
  @Getter private final String setting;
  @Getter private final int mean;
  @Getter private final int variance;

  public NormalDistributionVisualization(String setting, NormalDistributionConfig config) {
    this.setting = setting;
    mean = config.getMean();
    variance = config.getVariance();
  }
}
