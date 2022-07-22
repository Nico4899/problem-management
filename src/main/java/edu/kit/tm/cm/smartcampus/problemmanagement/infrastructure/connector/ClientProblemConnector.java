package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Collections;

/** This class represents an implementation of the {@link ProblemConnector} interface. */
@Component
public class ClientProblemConnector implements ProblemConnector {

  private final String baseUrl;
  private final RestTemplate restTemplate;

  @Value("${problem.listProblemsUrl}")
  private String listProblemsUrl;

  @Value("${problem.createProblemUrl}")
  private String createProblemUrl;

  @Value("${problem.getProblemUrl}")
  private String getProblemUrl;

  @Value("${problem.updateProblemUrl}")
  private String updateProblemUrl;

  @Value("${problem.removeProblemUrl}")
  private String removeProblemUrl;

  /**
   * Constructs a rest template problem connector.
   *
   * @param restTemplate rest template
   * @param baseUrl base url
   */
  @Autowired
  public ClientProblemConnector(
      RestTemplate restTemplate, @Value("${problem.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }

  @Override
  public Collection<Problem> listProblems() {
    ResponseEntity<Collection<Problem>> responseEntity;

    responseEntity =
        restTemplate.exchange(
            baseUrl + listProblemsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      return responseEntity.getBody();
    }
    return Collections.emptyList();
  }

  @Override
  public Problem createProblem(Problem problem) {
    ResponseEntity<Problem> responseEntity;

    responseEntity = restTemplate.postForEntity(baseUrl + createProblemUrl, problem, Problem.class);

    return responseEntity.getBody();
  }

  @Override
  public Problem getProblem(String identificationNumber) {
    ResponseEntity<Problem> responseEntity;

    responseEntity =
        restTemplate.getForEntity(baseUrl + getProblemUrl, Problem.class, identificationNumber);

    return responseEntity.getBody();
  }

  @Override
  public Problem updateProblem(Problem problem) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Problem> entity = new HttpEntity<>(problem, headers);

    restTemplate.exchange(
        baseUrl + updateProblemUrl,
        HttpMethod.PUT,
        entity,
        Void.class,
        problem.getIdentificationNumber());
    return problem;
  }

  @Override
  public void removeProblem(String identificationNumber) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(identificationNumber, headers);

    restTemplate.exchange(
        baseUrl + removeProblemUrl, HttpMethod.DELETE, entity, Void.class, identificationNumber);
  }
}
