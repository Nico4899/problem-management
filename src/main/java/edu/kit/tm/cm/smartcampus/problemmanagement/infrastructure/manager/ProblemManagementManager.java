package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.manager;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.operations.filter.options.FilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ProblemManagementManager {

  private static final String BIN_PATTERN = "b-\\d+";
  private static final String RIN_PATTERN = "r-\\d+";
  private static final String CIN_PATTERN = "c-\\d+";

  private final BuildingConnector buildingConnector;
  private final ProblemConnector problemConnector;

  @Autowired
  public ProblemManagementManager(
      BuildingConnector buildingConnector, ProblemConnector problemConnector) {
    this.buildingConnector = buildingConnector;
    this.problemConnector = problemConnector;
  }

  public Collection<Problem> listProblems(FilterOptions filterOptions) {
    Collection<Problem> problems = this.problemConnector.listProblems();
    filterOptions.executeProblemFilter(problems);
    return problems;
  }

  public Problem getProblem(String identificationNumber) {
    return this.problemConnector.getProblem(identificationNumber);
  }

  public Problem createProblem(Problem problem) {
    problem.setProblemState(ProblemState.OPEN);
    return this.problemConnector.createProblem(problem);
  }

  public Problem updateProblem(Problem problem) {
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      Notification notification = problem.extractNotification();
      this.buildingConnector.updateNotification(notification);
    }
    return this.problemConnector.updateProblem(problem);
  }

  public void removeProblem(String identificationNumber) {
    removeNotification(identificationNumber);
    this.problemConnector.removeProblem(identificationNumber);
  }

  public void acceptProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).accept();
    postNotification(identificationNumber);
  }

  public void declineProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).decline();
    removeNotification(identificationNumber);
  }

  public void closeProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).close();
    removeNotification(identificationNumber);
  }

  public void approachProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).approach();
  }

  public void holdProblem(String identificationNumber) {
    this.problemConnector.getProblem(identificationNumber).hold();
  }

  private void postNotification(String problemIdentificationNumber) {
    Problem problem = problemConnector.getProblem(problemIdentificationNumber);
    Notification notification = problem.extractNotification();
    if (notification.getParentIdentificationNumber().matches(BIN_PATTERN)) {
      notification = buildingConnector.createBuildingNotification(notification);
      problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
      updateProblem(problem);
    }
    if (notification.getParentIdentificationNumber().matches(RIN_PATTERN)) {
      notification = buildingConnector.createRoomNotification(notification);
      problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
      updateProblem(problem);
    }
    if (notification.getParentIdentificationNumber().matches(CIN_PATTERN)) {
      notification = buildingConnector.createComponentNotification(notification);
      problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
      updateProblem(problem);
    }
  }

  private void removeNotification(String problemIdentificationNumber) {
    Problem problem = this.problemConnector.getProblem(problemIdentificationNumber);
    if (!problem.getNotificationIdentificationNumber().isBlank()) {
      String notificationIdentificationNumber = problem.getNotificationIdentificationNumber();
      this.buildingConnector.removeNotification(notificationIdentificationNumber);
    }
  }
}
