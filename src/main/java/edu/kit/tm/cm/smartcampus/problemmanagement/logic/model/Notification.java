package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/** This class represents a notification. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
  private String identificationNumber;
  private String notificationTitle;
  private String notificationDescription;
  private Timestamp creationTime;
  private String parentIdentificationNumber;

  /**
   * Parse {@link Notification} from {@link Problem}.
   *
   * @param problem the problem to be parsed from
   * @return the notification parsed
   */
  public static Notification fromProblem(Problem problem) {
    return Notification.builder()
        .notificationDescription(problem.getProblemDescription())
        .creationTime(problem.getCreationTime())
        .identificationNumber(problem.getNotificationIdentificationNumber())
        .parentIdentificationNumber(problem.getReferenceIdentificationNumber())
        .notificationTitle(problem.getProblemTitle())
        .build();
  }
}
