package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

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

  public void accept() {
    this.problemState = this.problemState.accept();
  }

  public void decline() {
    this.problemState = this.problemState.decline();
  }

  public void close() {
    this.problemState = this.problemState.close();
  }

  public void hold() {
    this.problemState = this.problemState.hold();
  }

  public void approach() {
    this.problemState = this.problemState.approach();
  }
}
