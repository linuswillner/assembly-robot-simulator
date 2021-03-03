package com.assemblyrobot.shared.config.model;

import lombok.Getter;
import lombok.Setter;

public class StationConfig {
  @Getter @Setter private int assemblyStationAmount;
  @Getter @Setter private int errorCheckStationAmount;
  @Getter @Setter private int fittingFixStationAmount;
  @Getter @Setter private int boltingFixStationAmount;
  @Getter @Setter private int rivetingFixStationAmount;
  @Getter @Setter private int weldingFixStationAmount;
  @Getter @Setter private int returningFixStationAmount;
}
