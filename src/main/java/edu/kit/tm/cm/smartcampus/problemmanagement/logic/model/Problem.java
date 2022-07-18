package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/** The type Problem. */
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
