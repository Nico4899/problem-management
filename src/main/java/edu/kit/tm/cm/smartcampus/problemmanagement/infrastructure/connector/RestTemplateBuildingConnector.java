package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateBuildingConnector implements BuildingConnector{

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
  public RestTemplateBuildingConnector(RestTemplate restTemplate, @Value("${building.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public Notification createBuildingNotification(Notification notification) {
    ResponseEntity<Notification> responseEntity;

    responseEntity = restTemplate.postForEntity(baseUrl + createBuildingNotificationUrl, notification, Notification.class);

    return responseEntity.getBody();
  }

  @Override
  public Notification createRoomNotification(Notification notification) {
    ResponseEntity<Notification> responseEntity;

    responseEntity = restTemplate.postForEntity(baseUrl + createRoomNotificationUrl, notification, Notification.class);

    return responseEntity.getBody();
  }

  @Override
  public Notification createComponentNotification(Notification notification) {
    ResponseEntity<Notification> responseEntity;

    responseEntity = restTemplate.postForEntity(baseUrl + createComponentNotificationUrl, notification, Notification.class);

    return responseEntity.getBody();
  }

  @Override
  public void updateNotification(Notification notification) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Notification> entity = new HttpEntity<>(notification, headers);

    restTemplate.exchange(baseUrl + updateNotificationUrl, HttpMethod.PUT, entity, Void.class, notification.getIdentificationNumber());
  }

  @Override
  public void removeNotification(String identificationNumber) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(identificationNumber, headers);

    restTemplate.exchange(
            baseUrl + removeNotificationUrl, HttpMethod.DELETE, entity, Void.class, identificationNumber);
  }
}
