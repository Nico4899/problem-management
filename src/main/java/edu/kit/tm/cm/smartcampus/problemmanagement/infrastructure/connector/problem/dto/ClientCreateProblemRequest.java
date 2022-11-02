package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** This class acts as DTO to consume the Building Domain REST API */
@Getter
@Setter
@NoArgsConstructor
public class ClientCreateProblemRequest {
  private String title;
  private String description;
  private String reporter;
  private String referenceIdentificationNumber;
  private String notificationIdentificationNumber;
}
