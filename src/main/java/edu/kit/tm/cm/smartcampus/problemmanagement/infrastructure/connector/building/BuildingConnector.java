package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientCreateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientUpdateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;

/**
 * This interface describes a connector, which connects this microservice to the building domain
 * service.
 */
public interface BuildingConnector {
  /**
   * Create a new notification.
   *
   * @param clientCreateNotificationRequest notification to be created
   * @return created notification
   */
  Notification createNotification(ClientCreateNotificationRequest clientCreateNotificationRequest);

  /**
   * Update a notification.
   *
   * @param clientUpdateNotificationRequest notification to be updated.
   */
  void updateNotification(ClientUpdateNotificationRequest clientUpdateNotificationRequest);

  /**
   * Remove a notification.
   *
   * @param identificationNumber identification number belonging to the notification to be removed.
   */
  void removeNotification(String identificationNumber);
}
