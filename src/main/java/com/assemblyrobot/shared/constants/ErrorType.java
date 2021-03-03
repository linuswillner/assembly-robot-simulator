package com.assemblyrobot.shared.constants;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ErrorFixTimeConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorType {
  FITTING(FixTimes.config.getFittingFixTime()),
  BOLTING(FixTimes.config.getBoltingFixTime()),
  RIVETING(FixTimes.config.getRivetingFixTime()),
  WELDING(FixTimes.config.getWeldingFixTime()),
  RETURNING(FixTimes.config.getReturningFixTime());

  @Getter private final long fixTime;

  // Using inner class as a workaround to get the fix times to the enum, otherwise we get hit with
  // an "illegal forward reference" error (See https://stackoverflow.com/a/30169296)
  private static class FixTimes {
    private static final ErrorFixTimeConfig config = Config.getConfig().getErrorFixTimes();
  }
}
