package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.PSFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * This class represents the core of this microservice. It contains of all logic, operations and
 * model.
 */
@Service
public class ProblemManagementService {

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
  public ProblemManagementService(
      BuildingConnector buildingConnector, ProblemConnector problemConnector) {
    this.buildingConnector = buildingConnector;
    this.problemConnector = problemConnector;
  }

  /**
   * List all problems and filter them by provided filter options.
   *
   * @param filterOptions filter options used to filter the results
   * @return filtered problems
   */
  public Collection<Problem> listProblems(FilterOptions filterOptions) {
    Collection<Problem> problems = this.problemConnector.listProblems();
    if (filterOptions.getProblemStateFilterOption().isSelected()) {
      Filter<Problem> filter =
          new PSFilter(filterOptions.getProblemStateFilterOption().getFilterValues());
      problems = filter.filter(problems);
    }
    return problems;
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
    return this.problemConnector.createProblem(problem);
  }

  /**
   * Update a problem.
   *
   * @param problem problem to be updated
   * @return updated problem
   */
  public Problem updateProblem(Problem problem) {
    Problem updatedProblem = this.problemConnector.updateProblem(problem);
    updateNotification(updatedProblem);
    return updatedProblem;
  }

  /**
   * remove a problem.
   *
   * @param identificationNumber problem identification number of problem to be removed
   */
  public void removeProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    this.problemConnector.removeProblem(identificationNumber);
    this.removeNotification(problem);
  }

  /**
   * Accept problem.
   *
   * @param identificationNumber problem identification number
   */
  public void acceptProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.accept();
    this.postNotification(problem);
  }

  /**
   * Decline problem.
   *
   * @param identificationNumber problem identification number
   */
  public void declineProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.decline();
    this.removeNotification(problem);
  }

  /**
   * Close problem.
   *
   * @param identificationNumber problem identification number
   */
  public void closeProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.close();
    this.removeNotification(problem);
  }

  /**
   * Approach problem.
   *
   * @param identificationNumber problem identification number
   */
  public void approachProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).approach();
  }

  /**
   * Hold problem.
   *
   * @param identificationNumber problem identification number
   */
  public void holdProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).hold();
  }

  private void postNotification(Problem problem) {
    Notification notification =
        this.buildingConnector.createNotification(Notification.fromProblem(problem));
    problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
    this.updateProblem(problem);
  }

  private void updateNotification(Problem problem) {
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      this.buildingConnector.updateNotification(Notification.fromProblem(problem));
    }
  }

  private void removeNotification(Problem problem) {
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      this.buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
      problem.setNotificationIdentificationNumber(BLANK_STRING);
    }
  }
}
