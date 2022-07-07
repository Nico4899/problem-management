package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Open implements State {

  @Override
  public ProblemState accept() {
    return ProblemState.ACCEPTED;
  }

  @Override
  public ProblemState approach() {
    return ProblemState.OPEN;
  }

  @Override
  public ProblemState close() {
    return ProblemState.OPEN;
  }

  @Override
  public ProblemState hold() {
    return ProblemState.OPEN;
  }

  @Override
  public ProblemState decline() {
    return ProblemState.DECLINED;
  }
}
