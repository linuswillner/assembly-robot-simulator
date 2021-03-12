package com.assemblyrobot.ui.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Visualisation transformer class for {@link
 * com.assemblyrobot.shared.config.model.ErrorFixTimeConfig}s.
 */
@RequiredArgsConstructor
public class ErrorFixTimeVisualization {
  @Getter private final String errorType;
  @Getter private final int fixTime;
}
