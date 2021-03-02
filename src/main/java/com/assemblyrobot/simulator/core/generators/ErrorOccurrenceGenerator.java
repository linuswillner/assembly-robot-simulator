package com.assemblyrobot.simulator.core.generators;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ErrorFixTimeConfig;
import com.assemblyrobot.shared.config.model.ErrorOccurrenceConfig;
import com.assemblyrobot.shared.constants.ErrorType;
import eduni.distributions.Negexp;
import eduni.distributions.Uniform;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;

/**
 * Error occurrence generator. This generator decides whether A. There should be an error in the
 * current material based on a {@link Uniform} distribution (as a "one in X" chance), and, if so,
 * provides tools to obtain B. What the {@link ErrorType} for that error is based on a {@link
 * Negexp} distribution.
 *
 * <p>The parameters for both procedures have defaults provided by the system and are alterable by
 * the user.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorOccurrenceGenerator {
  @Getter private static final ErrorOccurrenceGenerator instance = new ErrorOccurrenceGenerator();

  private final ErrorOccurrenceConfig occurrenceConfig =
      Config.getConfig().getErrorOccurrenceParams();
  private static final ErrorFixTimeConfig fixTimeConfig = Config.getConfig().getErrorFixTimes();

  private final Uniform errorOccurrenceGenerator =
      new Uniform(1, occurrenceConfig.getErrorThreshold());
  private final Negexp errorTypeGenerator = new Negexp(occurrenceConfig.getTypeDistributionMean());

  // Note: Starting at 1 so the error threshold can be measured as one in two/three/four... (Def. 3)

  public boolean shouldHaveError() {
    return Math.round(errorOccurrenceGenerator.sample()) >= occurrenceConfig.getErrorThreshold();
  }

  public ErrorType nextError() {
    val errors = ErrorType.values();

    // Get an error index from the array, or the last one if we somehow went over the array bounds
    val errorIndex = (int) Math.min(Math.round(errorTypeGenerator.sample()), errors.length - 1);

    return errors[errorIndex];
  }
}
