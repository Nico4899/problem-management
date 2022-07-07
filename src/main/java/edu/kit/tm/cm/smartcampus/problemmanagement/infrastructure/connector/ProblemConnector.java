package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;

import java.util.Collection;

public interface ProblemConnector {
  Collection<Problem> listProblems();

  Problem createProblem(Problem problem);

  Problem getProblem(String identificationNumber);

  Problem updateProblem(Problem problem);

  void removeProblem(String identificationNumber);
}
