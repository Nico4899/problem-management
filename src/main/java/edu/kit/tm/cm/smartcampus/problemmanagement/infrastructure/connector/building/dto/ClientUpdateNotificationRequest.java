package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientUpdateNotificationRequest {
  private String identificationNumber;
  private String title;
  private String description;
  private String parentIdentificationNumber;
}
