package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.settings.Settings;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.utility.DataTransferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This class represents the core of this microservice. It contains of all logic, operations and
 * model.
 */
@Component
public class Service {

  /** The constant BLANK_STRING. */
  public static final String BLANK_STRING = "";

  private final BuildingConnector buildingConnector;
  private final ProblemConnector problemConnector;

  /**
   * Constructs a new problem management service.
   *
   * @param buildingConnector building connector (constructor injection)
   * @param problemConnector problem connector (constructor injection)
   */
  @Autowired
  public Service(BuildingConnector buildingConnector, ProblemConnector problemConnector) {
    this.buildingConnector = buildingConnector;
    this.problemConnector = problemConnector;
  }

  /**
   * List all problems and filter them by provided filter options.
   *
   * @param configuration configuration for listing
   * @return filtered/ sorted problems
   */
  public Collection<Problem> listProblems(Settings<Problem> configuration) {
    return configuration.apply(this.problemConnector.listProblems());
  }

  /**
   * Get a specific problem.
   *
   * @param identificationNumber problem identification number
   * @return requested problem
   */
  public Problem getProblem(String identificationNumber) {
    return this.problemConnector.getProblem(identificationNumber);
  }

  /**
   * Create a new problem.
   *
   * @param problem problem to be created
   * @return created problem
   */
  public Problem createProblem(Problem problem) {
    return this.problemConnector.createProblem(DataTransferUtils.ClientRequestWriter.writeCreateProblemRequest(problem));
  }

  /**
   * Update a problem.
   *
   * @param problem problem to be updated
   * @return updated problem
   */
  public Problem updateProblem(Problem problem) {
    Problem updatedProblem = this.problemConnector.getProblem(problem.getIdentificationNumber());
    updatedProblem.setTitle(problem.getTitle());
    updatedProblem.setReferenceIdentificationNumber(problem.getReferenceIdentificationNumber());
    updatedProblem.setDescription(problem.getDescription());
    updatedProblem.setNotificationIdentificationNumber(
        problem.getNotificationIdentificationNumber());
    updateNotification(updatedProblem);
    return this.problemConnector.updateProblem(DataTransferUtils.ClientRequestWriter.writeUpdateProblemRequest(updatedProblem));
  }

  /**
   * remove a problem.
   *
   * @param identificationNumber problem identification number of problem to be removed
   */
  public void removeProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    this.problemConnector.removeProblem(identificationNumber);
    if (problem.getNotificationIdentificationNumber() != null) {
      this.buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
      problem.setNotificationIdentificationNumber(null);
    }
  }

  /**
   * Accept problem.
   *
   * @param identificationNumber problem identification number
   */
  public void changeState(String identificationNumber, Problem.State.Operation operation) {
    operation.apply(
        this.problemConnector.getProblem(identificationNumber),
        this.buildingConnector,
        this.problemConnector);
  }

  private void updateNotification(Problem problem) {
    if (problem.getNotificationIdentificationNumber() != null) {
      this.buildingConnector.updateNotification(DataTransferUtils.ClientRequestWriter.writeUpdateNotificationRequest(problem));
    }
  }
}
