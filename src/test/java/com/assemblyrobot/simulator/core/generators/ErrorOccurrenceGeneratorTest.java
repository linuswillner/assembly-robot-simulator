package com.assemblyrobot.simulator.core.generators;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assemblyrobot.shared.config.Config;
import com.assemblyrobot.shared.config.model.ErrorOccurrenceConfig;
import com.assemblyrobot.shared.constants.ErrorType;
import java.util.ArrayList;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ErrorOccurrenceGeneratorTest {
  private final ErrorOccurrenceGenerator generator = ErrorOccurrenceGenerator.getInstance();
  private final ErrorOccurrenceConfig config = Config.getConfig().getErrorOccurrenceParams();
  private static final int SAMPLE_SIZE = 100;

  @Test
  @DisplayName("shouldHaveError(): An error has a \"1 in X\" chance of happening")
  void shouldHaveError() {
    // Define a desired percentage of error occurrences based on the threshold
    val targetPercentage = 1.0 / config.getErrorThreshold();
    val errorOccurrences = new ArrayList<Boolean>();

    for (int i = 0; i < SAMPLE_SIZE; i++) {
      val hasError = generator.shouldHaveError();

      if (hasError) {
        errorOccurrences.add(true);
      }
    }

    // Define a target array size based on the target percentage of the sample size
    val targetSize = Math.floor(targetPercentage * SAMPLE_SIZE);

    // A 1-2 error distribution delta is within the realm of feasibility, but much more than that
    // would make this test too unreliable to perform
    assertEquals(targetSize, errorOccurrences.size(), 2.0);
  }

  // Not testing nextError() in detail here, predicting negexp distributions and their relations to
  // generated enums is slightly beyond the realm of feasibility (Too unreliable)
  @Test
  @DisplayName("nextError(): Returns a correct ErrorType object")
  void nextError() {
    MatcherAssert.assertThat(generator.nextError(), instanceOf(ErrorType.class));
  }
}
