package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;

import java.util.Collection;

/**
 * This interface describes a connector, which connects this microservice to the problem domain
 * service.
 */
public interface ProblemConnector {

  /**
   * List all problems from the domain service.
   *
   * @return collection of problems
   */
  Collection<Problem> listProblems();

  /**
   * Create a new problem.
   *
   * @param problem problem to be crated
   * @return created problem
   */
  Problem createProblem(Problem problem);

  /**
   * Get a specific problem by its identification number.
   *
   * @param identificationNumber identification number of the requested problem
   * @return the requested problem
   */
  Problem getProblem(String identificationNumber);

  /**
   * Update a problem.
   *
   * @param problem problem to be updated
   * @return updated problem
   */
  Problem updateProblem(Problem problem);

  /**
   * Remove a problem.
   *
   * @param identificationNumber identification number of the problem to be removed
   */
  void removeProblem(String identificationNumber);
}
