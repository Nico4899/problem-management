package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

public interface State {
  ProblemState accept();
  ProblemState approach();
  ProblemState close();
  ProblemState hold();
  ProblemState decline();
}
