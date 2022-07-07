package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.state.ProblemState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Problem {
  private ProblemState problemState;
  private String problemTitle;
  private String problemDescription;
  private Timestamp creationTime;
  private String problemReporter;
  private String identificationNumber;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;

  public Notification extractNotification() {
    Notification extractedNotification = new Notification();
    extractedNotification.setNotificationDescription(problemDescription);
    extractedNotification.setNotificationTitle(problemTitle);
    extractedNotification.setCreationTime(creationTime);
    extractedNotification.setParentIdentificationNumber(referenceIdentificationNumber);
    extractedNotification.setIdentificationNumber(notificationIdentificationNumber);
    return extractedNotification;
  }

  public void accept() {
    this.problemState = this.problemState.getState().accept();
  }

  public void decline() {
    this.problemState = this.problemState.getState().decline();
  }

  public void close() {
    this.problemState = this.problemState.getState().close();
  }

  public void hold() {
    this.problemState = this.problemState.getState().hold();
  }

  public void approach() {
    this.problemState = this.problemState.getState().approach();
  }
}
