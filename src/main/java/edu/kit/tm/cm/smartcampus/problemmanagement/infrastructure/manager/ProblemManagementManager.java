package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.manager;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.BuildingConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.ProblemConnector;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.ProblemState;
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
  public ProblemManagementManager(BuildingConnector buildingConnector, ProblemConnector problemConnector) {
    this.buildingConnector = buildingConnector;
    this.problemConnector = problemConnector;
  }

  public Collection<Problem> listProblems() {
    return this.problemConnector.listProblems();

    //list all problems from te Problem-Domain
  }

  public Problem getProblem(String identificationNumber) {
    return this.problemConnector.getProblem(identificationNumber);

    // get one problem from the Problem-Domain
  }

  public Problem createProblem(Problem problem) {
    return this.problemConnector.createProblem(problem);

    // create a new problem in the Problem-Domain
  }

  public Problem updateProblem(Problem problem) {
    if (problem.getProblemState() == ProblemState.ACCEPTED) {
      if (problem.getNotificationIdentificationNumber().isEmpty()) {
        if (problem.getReferenceIdentificationNumber().matches(BIN_PATTERN)) {
          Notification notification = this.buildingConnector.createBuildingNotification(problem.extractNotification());
          problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
        }
        if (problem.getReferenceIdentificationNumber().matches(RIN_PATTERN)) {
          Notification notification = this.buildingConnector.createRoomNotification(problem.extractNotification());
          problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
        }
        if (problem.getReferenceIdentificationNumber().matches(CIN_PATTERN)) {
          Notification notification = this.buildingConnector.createComponentNotification(problem.extractNotification());
          problem.setNotificationIdentificationNumber(notification.getIdentificationNumber());
        }
      }
      this.buildingConnector.updateNotification(problem.extractNotification());
    }
    if (problem.getProblemState() == ProblemState.CLOSED) {
      this.buildingConnector.removeNotification(problem.getNotificationIdentificationNumber());
    }
    return this.problemConnector.updateProblem(problem);

    // change one problem, in case the state was changed from OPEN/DECLINED -> ACCEPTED then add a new Notification to Building-Domain
    // if it was DECLINED/IN_PROGRESS and was changed to CLOSED remove it from Building-Domain
    // if it was in ACCEPTED -> DECLINED then remove it from Building-Domain

    // maybe state machine model implementation with boolean if statechange happened
    // then Service could tell frontend possible actions
  }

  public void removeProblem(String identificationNumber) {
    this.buildingConnector.removeNotification(this.problemConnector.getProblem(identificationNumber).getNotificationIdentificationNumber());
    this.problemConnector.removeProblem(identificationNumber);

    // remove connected notification and then remove problem itself
  }

  public void acceptProblem(String identificationNumber) {

  }

  public void declineProblem(String identificationNumber) {

  }

  public void closeProblem(String identificationNumber) {

  }

  public void approachProblem(String identificationNumber) {

  }

  public void holdProblem(String identificationNumber) {

  }

}
