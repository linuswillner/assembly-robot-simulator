package com.assemblyrobot.ui.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorFixTimeVisualization {
  @Getter private final String errorType;
  @Getter private final int fixTime;
}
