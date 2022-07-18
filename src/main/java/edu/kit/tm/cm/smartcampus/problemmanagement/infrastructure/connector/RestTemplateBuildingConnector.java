package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/** This class represents an implementation of the {@link BuildingConnector} interface. */
@Component
public class RestTemplateBuildingConnector implements BuildingConnector {

  private final String baseUrl;
  private final RestTemplate restTemplate;

  @Value("${notification.createNotificationUrl}")
  private String createComponentNotificationUrl;

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
  public RestTemplateBuildingConnector(
      RestTemplate restTemplate, @Value("${building.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public Notification createNotification(Notification notification) {
    ResponseEntity<Notification> responseEntity;

    responseEntity =
        restTemplate.postForEntity(
            baseUrl + createComponentNotificationUrl, notification, Notification.class);

    return responseEntity.getBody();
  }

  @Override
  public Notification updateNotification(Notification notification) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Notification> entity = new HttpEntity<>(notification, headers);

    restTemplate.exchange(
        baseUrl + updateNotificationUrl,
        HttpMethod.PUT,
        entity,
        Void.class,
        notification.getIdentificationNumber());
    return notification;
  }

  @Override
  public void removeNotification(String identificationNumber) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(identificationNumber, headers);

    restTemplate.exchange(
        baseUrl + removeNotificationUrl,
        HttpMethod.DELETE,
        entity,
        Void.class,
        identificationNumber);
  }
}
