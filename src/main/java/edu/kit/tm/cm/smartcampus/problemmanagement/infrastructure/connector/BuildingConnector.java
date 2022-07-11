package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;

public interface BuildingConnector {
  Notification createNotification(Notification notification);

  Notification updateNotification(Notification notification);

  void removeNotification(String identificationNumber);
}
