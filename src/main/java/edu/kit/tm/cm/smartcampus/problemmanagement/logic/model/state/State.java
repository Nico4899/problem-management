package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;

public interface State {
  ProblemState accept();

  ProblemState approach();

  ProblemState close();

  ProblemState hold();

  ProblemState decline();
}
