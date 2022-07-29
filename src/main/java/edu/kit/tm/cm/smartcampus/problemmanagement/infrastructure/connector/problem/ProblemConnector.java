package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientCreateProblemRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientUpdateProblemRequest;
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
   * @param clientCreateProblemRequest problem to be crated
   * @return created problem
   */
  Problem createProblem(ClientCreateProblemRequest clientCreateProblemRequest);

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
   * @param clientUpdateProblemRequest problem to be updated
   * @return updated problem
   */
  Problem updateProblem(ClientUpdateProblemRequest clientUpdateProblemRequest);

  /**
   * Remove a problem.
   *
   * @param identificationNumber identification number of the problem to be removed
   */
  void removeProblem(String identificationNumber);
}
