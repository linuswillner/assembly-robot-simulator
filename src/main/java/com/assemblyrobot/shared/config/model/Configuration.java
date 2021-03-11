package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Configuration {
  @Getter @Setter private NormalDistributionConfig arrivalIntervalParams;
  @Getter @Setter private NormalDistributionConfig assemblyTimeParams;
  @Getter @Setter private NormalDistributionConfig errorCheckTimeParams;
  @Getter @Setter private ErrorOccurrenceConfig errorOccurrenceParams;
  @Getter @Setter private ErrorFixTimeConfig errorFixTimes;
  @Getter @Setter private StationConfig stationParams;
  @Getter @Setter private AppConfig appSettings;
}
