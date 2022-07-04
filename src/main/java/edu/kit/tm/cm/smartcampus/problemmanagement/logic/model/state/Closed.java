package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Closed implements State{

  @Override
  public ProblemState accept() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState approach() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState close() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState hold() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState decline() {
    return ProblemState.CLOSED;
  }

}
