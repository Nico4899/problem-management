package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Problem {
  private ProblemState problemState;
  private String problemTitle;
  private String problemDescription;
  private Date creationDate;
  private String problemReporter;
  private String identificationNumber;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;

  public Notification extractNotification() {
    Notification extractedNotification = new Notification();
    extractedNotification.setNotificationDescription(problemDescription);
    extractedNotification.setNotificationTitle(problemTitle);
    extractedNotification.setCreationTime(creationDate);
    extractedNotification.setParentIdentificationNumber(referenceIdentificationNumber);
    extractedNotification.setIdentificationNumber(notificationIdentificationNumber);
    return extractedNotification;
  }
}
