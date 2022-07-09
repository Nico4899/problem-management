package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.Filter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.filters.PSFilter;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ProblemManagementService {

  private final BuildingConnector buildingConnector;
  private final ProblemConnector problemConnector;

  @Autowired
  public ProblemManagementService(
      BuildingConnector buildingConnector, ProblemConnector problemConnector) {
    this.buildingConnector = buildingConnector;
    this.problemConnector = problemConnector;
  }

  public Collection<Problem> listProblems(FilterOptions filterOptions) {
    Collection<Problem> problems = this.problemConnector.listProblems();
    if (filterOptions.getProblemStateFilterOption().isSelected()) {
      Filter<Problem, ProblemState> filter = new PSFilter();
      filter.setFilterValues(filterOptions.getProblemStateFilterOption().getFilterValues());
      problems = filter.filter(problems);
    }
    return problems;
  }

  public Problem getProblem(String identificationNumber) {
    return this.problemConnector.getProblem(identificationNumber);
  }

  public Problem createProblem(Problem problem) {
    return this.problemConnector.createProblem(problem);
  }

  public Problem updateProblem(Problem problem) {
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      Notification notification = Notification.fromProblem(problem);
      this.buildingConnector.updateNotification(notification);
    }
    return this.problemConnector.updateProblem(problem);
  }

  public void removeProblem(String identificationNumber) {
    this.removeNotification(identificationNumber);
    this.problemConnector.removeProblem(identificationNumber);
  }

  public void acceptProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).accept();
    this.postNotification(identificationNumber);
  }

  public void declineProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).decline();
    this.removeNotification(identificationNumber);
  }

  public void closeProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).close();
    this.removeNotification(identificationNumber);
  }

  public void approachProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).approach();
  }

  public void holdProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).hold();
  }

  private void postNotification(String problemIdentificationNumber) {
    Problem problem = this.problemConnector.getProblem(problemIdentificationNumber);
    Notification notification = Notification.fromProblem(problem);
    notification = this.buildingConnector.createNotification(notification);
    problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
    this.updateProblem(problem);
  }

  private void removeNotification(String problemIdentificationNumber) {
    Problem problem = this.problemConnector.getProblem(problemIdentificationNumber);
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      String notificationIdentificationNumber = problem.getNotificationIdentificationNumber();
      this.buildingConnector.removeNotification(notificationIdentificationNumber);
    }
  }
}
