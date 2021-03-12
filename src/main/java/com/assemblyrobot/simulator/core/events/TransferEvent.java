package com.assemblyrobot.simulator.core.events;

import com.assemblyrobot.shared.constants.ErrorType;
import com.assemblyrobot.shared.constants.StageID;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * Unique {@link Event} subclass for {@link EventType#TRANSFER} events, which require information to
 * be passed on the destination of a {@link com.assemblyrobot.simulator.system.components.Material}.
 */
@ToString
public class TransferEvent extends Event {
  @Getter private final long executionTime;
  @Getter @NonNull private final EventType type;
  @Getter @NonNull private final StageID current;
  @Getter @NonNull private final StageID destination;
  @Getter private final ErrorType error;

  // Using a legacy constructor here because we need to partially call super()
  public TransferEvent(
      long executionTime,
      @NonNull EventType type,
      @NonNull StageID currentStage,
      @NonNull StageID destination,
      ErrorType optionalError) {
    super(executionTime, type);
    this.executionTime = executionTime;
    this.type = type;
    this.current = currentStage;
    this.destination = destination;
    this.error = optionalError;
  }
}
