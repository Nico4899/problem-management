package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Accepted implements State {

  @Override
  public ProblemState accept() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState approach() {
    return ProblemState.IN_PROGRESS;
  }

  @Override
  public ProblemState close() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState hold() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState decline() {
    return ProblemState.DECLINED;
  }
}
