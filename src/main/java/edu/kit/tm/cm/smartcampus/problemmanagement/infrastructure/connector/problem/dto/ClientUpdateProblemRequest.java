package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ClientUpdateProblemRequest {
  private Problem.State state;
  private String title;
  private String description;
  private String reporter;
  private String identificationNumber;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;
}
