package com.assemblyrobot.ui.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Visualisation transformer class for {@link
 * com.assemblyrobot.shared.config.model.StationConfig}s.
 */
@RequiredArgsConstructor
public class StationAmountVisualization {
  @Getter private final String stationType;
  @Getter private final int amount;
}
