package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.*;

import java.sql.Timestamp;

/** This class represents a notification. */
@Getter
@Setter
@NoArgsConstructor
public class Notification {
  private String identificationNumber;
  private String title;
  private String description;
  private Timestamp creationTime;
  private String parentIdentificationNumber;
}
