package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateBuildingConnector implements BuildingConnector {

  @Value("notification.createBuildingNotificationUrl")
  private String createBuildingNotificationUrl;

  @Value("notification.createRoomNotificationUrl")
  private String createRoomNotificationUrl;

  @Value("notification.createComponentNotificationUrl")
  private String createComponentNotificationUrl;

  @Value("notification.updateNotificationUrl")
  private String updateNotificationUrl;

  @Value("notification.removeNotificationUrl")
  private String removeNotificationUrl;

  private final String baseUrl;
  private final RestTemplate restTemplate;

  @Autowired
  public RestTemplateBuildingConnector(
      RestTemplate restTemplate, @Value("${building.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public Notification createBuildingNotification(Notification notification) {
    return null;
  }

  @Override
  public Notification createRoomNotification(Notification notification) {
    return null;
  }

  @Override
  public Notification createComponentNotification(Notification notification) {
    return null;
  }

  @Override
  public void updateNotification(Notification notification) {}

  @Override
  public void removeNotification(String identificationNumber) {}
}
