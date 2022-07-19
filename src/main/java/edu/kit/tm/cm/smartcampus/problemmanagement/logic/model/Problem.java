package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.*;

import java.sql.Timestamp;

/** The type Problem. */
@Builder
@Getter
@Setter
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

  /** Accept. */
  public void accept() {
    this.problemState = this.problemState.accept();
  }

  /** Decline. */
  public void decline() {
    this.problemState = this.problemState.decline();
  }

  /** Close. */
  public void close() {
    this.problemState = this.problemState.close();
  }

  /** Hold. */
  public void hold() {
    this.problemState = this.problemState.hold();
  }

  /** Approach. */
  public void approach() {
    this.problemState = this.problemState.approach();
  }
}
