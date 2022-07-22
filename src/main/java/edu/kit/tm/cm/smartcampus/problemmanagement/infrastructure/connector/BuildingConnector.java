package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;

/**
 * This interface describes a connector, which connects this microservice to the building domain
 * service.
 */
public interface BuildingConnector {
  /**
   * Create a new notification.
   *
   * @param notification notification to be created
   * @return created notification
   */
  Notification createNotification(Notification notification);

  /**
   * Update a notification.
   *
   * @param notification notification to be updated.
   */
  void updateNotification(Notification notification);

  /**
   * Remove a notification.
   *
   * @param identificationNumber identification number belonging to the notification to be removed.
   */
  void removeNotification(String identificationNumber);
}
