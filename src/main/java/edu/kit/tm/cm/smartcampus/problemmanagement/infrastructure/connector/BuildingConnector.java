package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;

public interface BuildingConnector {
  Notification createNotification(Notification notification);

  void removeNotification(String identificationNumber);
}
