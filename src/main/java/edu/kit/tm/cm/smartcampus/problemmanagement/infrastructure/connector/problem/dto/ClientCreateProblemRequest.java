package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientCreateProblemRequest {
  private String title;
  private String description;
  private String reporter;
  private String referenceIdentificationNumber;
}
