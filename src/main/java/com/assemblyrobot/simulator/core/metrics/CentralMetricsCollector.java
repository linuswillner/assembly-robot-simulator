package com.assemblyrobot.simulator.core.metrics;

import com.assemblyrobot.simulator.core.metrics.errors.DuplicateMetricsCollectorRegistrationError;
import java.util.HashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Centralised metrics collector. Used for keeping track of all metrics collectors in the simulator.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CentralMetricsCollector {
  @Getter private static final CentralMetricsCollector instance = new CentralMetricsCollector();

  @Getter private static final HashMap<String, MetricsCollector> collectors = new HashMap<>();

  /**
   * Registers a metrics collector. This method is automatically called by the default constructor of
   * {@link MetricsCollector}.
   *
   * <p>Note: With respect to the above, do not call this method manually. It is public strictly for
   * practical reasons.
   *
   * @param hostName Name of the host for this metrics collector; needs to be a unique identiifer.
   * @param collector {@link MetricsCollector}
   * @throws DuplicateMetricsCollectorRegistrationError If a metrics collector for this host class
   *     has already been registered.
   */
  public void registerMetricsCollector(
      @NonNull String hostName, @NonNull MetricsCollector collector)
      throws DuplicateMetricsCollectorRegistrationError {
    if (collectors.containsKey(hostName)) {
      throw new DuplicateMetricsCollectorRegistrationError(hostName);
    }

    collectors.put(hostName, collector);
  }

  /**
   * Dumps all current metrics collectors. Used for resets between simulator runs.
   */
  public void dump() {
    collectors.clear();
  }
}
