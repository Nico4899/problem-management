package edu.kit.tm.cm.smartcampus.problemmanagement.logic.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Notification {
  private String identificationNumber;
  private String notificationTitle;
  private String notificationDescription;
  private Date creationTime;
  private String parentIdentificationNumber;
}
