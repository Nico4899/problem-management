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
import org.springframework.util.StringUtils;

import java.util.Collection;

@Component
public class ProblemManagementService {

  public static final String BLANK_STRING = "";

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
    Problem updatedProblem =  this.problemConnector.updateProblem(problem);
    updateNotification(updatedProblem);
    return updatedProblem;
  }

  public void removeProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    this.problemConnector.removeProblem(identificationNumber);
    this.removeNotification(problem);
  }

  public void acceptProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.accept();
    this.postNotification(problem);
  }

  public void declineProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.decline();
    this.removeNotification(problem);
  }

  public void closeProblem(String identificationNumber) {
    Problem problem = this.problemConnector.getProblem(identificationNumber);
    problem.close();
    this.removeNotification(problem);
  }

  public void approachProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).approach();
  }

  public void holdProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).hold();
  }

  private void postNotification(Problem problem) {
    Notification notification = this.buildingConnector.createNotification(Notification.fromProblem(problem));
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
