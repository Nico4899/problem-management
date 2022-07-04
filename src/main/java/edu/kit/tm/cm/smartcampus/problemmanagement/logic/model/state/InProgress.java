package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InProgress implements State {

  @Override
  public ProblemState accept() {
    return ProblemState.IN_PROGRESS;
  }

  @Override
  public ProblemState approach() {
    return ProblemState.IN_PROGRESS;
  }

  @Override
  public ProblemState close() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState hold() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState decline() {
    return ProblemState.IN_PROGRESS;
  }
}
