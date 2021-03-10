package com.assemblyrobot.ui.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StationAmountVisualization {
  @Getter private final String stationType;
  @Getter private final int amount;
}
