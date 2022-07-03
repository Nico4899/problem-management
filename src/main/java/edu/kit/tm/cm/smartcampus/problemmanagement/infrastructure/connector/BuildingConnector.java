package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;

public interface BuildingConnector {
  Notification createBuildingNotification(Notification notification);
  Notification createRoomNotification(Notification notification);
  Notification createComponentNotification(Notification notification);
  Notification updateNotification(Notification notification);
  void removeNotification(String identificationNumber);
}