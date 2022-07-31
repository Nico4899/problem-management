package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientCreateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.building.dto.ClientUpdateNotificationRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** This class represents an implementation of the {@link BuildingConnector} interface. */
@Component
public class ClientBuildingConnector implements BuildingConnector {

  private final String baseUrl;
  private final RestTemplate restTemplate;

  @Value("${notification.createNotificationUrl}")
  private String createNotificationUrl;

  @Value("${notification.updateNotificationUrl}")
  private String updateNotificationUrl;

  @Value("${notification.removeNotificationUrl}")
  private String removeNotificationUrl;

  /**
   * Constructs a rest template building connector.
   *
   * @param restTemplate rest template used to parse server responses and send requests
   * @param baseUrl base url of the server to which requests are sent
   */
  @Autowired
  public ClientBuildingConnector(
      RestTemplate restTemplate, @Value("${building.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public Notification createNotification(
      ClientCreateNotificationRequest clientCreateNotificationRequest) {
    return restTemplate
        .exchange(
            baseUrl + createNotificationUrl,
            HttpMethod.POST,
            new HttpEntity<>(clientCreateNotificationRequest),
            Notification.class)
        .getBody();
  }

  @Override
  public void updateNotification(ClientUpdateNotificationRequest clientUpdateNotificationRequest) {
    restTemplate.exchange(
        baseUrl + updateNotificationUrl,
        HttpMethod.PUT,
        new HttpEntity<>(clientUpdateNotificationRequest),
        Void.class);
  }

  @Override
  public void removeNotification(String identificationNumber) {
    restTemplate.exchange(
        baseUrl + removeNotificationUrl,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Void.class,
        identificationNumber);
  }
}
