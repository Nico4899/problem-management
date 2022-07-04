package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import java.util.Collection;
import java.util.List;

public enum ProblemState {
  ACCEPTED(new Accepted(), List.of(StateOperation.APPROACH, StateOperation.DECLINE)),
  DECLINED(new Declined(), List.of(StateOperation.DECLINE, StateOperation.ACCEPT)),
  IN_PROGRESS(new InProgress(), List.of(StateOperation.CLOSE, StateOperation.HOLD)),
  OPEN(new Open(), List.of(StateOperation.DECLINE, StateOperation.ACCEPT)),
  CLOSED(new Closed(), List.of());

  private final State state;
  private final Collection<StateOperation> possibleOperations;

  ProblemState(State state, Collection<StateOperation> possibleOperations) {
    this.state = state;
    this.possibleOperations = possibleOperations;
  }

  public Collection<StateOperation> getPossibleOperations() {
    return this.possibleOperations;
  }

  public State getState() {
    return this.state;
  }
}
