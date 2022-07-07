package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Declined implements State {

  @Override
  public ProblemState accept() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState approach() {
    return ProblemState.DECLINED;
  }

  @Override
  public ProblemState close() {
    return ProblemState.CLOSED;
  }

  @Override
  public ProblemState hold() {
    return ProblemState.DECLINED;
  }

  @Override
  public ProblemState decline() {
    return ProblemState.DECLINED;
  }
}
