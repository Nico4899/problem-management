package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientCreateProblemRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.problem.dto.ClientUpdateProblemRequest;
import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

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
    return restTemplate
        .exchange(
            baseUrl + listProblemsUrl,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<Collection<Problem>>() {})
        .getBody();
  }

  @Override
  public Problem createProblem(ClientCreateProblemRequest clientCreateProblemRequest) {
    return restTemplate
        .exchange(
            baseUrl + createProblemUrl,
            HttpMethod.POST,
            new HttpEntity<>(clientCreateProblemRequest),
            Problem.class)
        .getBody();
  }

  @Override
  public Problem getProblem(String identificationNumber) {
    return restTemplate
        .exchange(
            baseUrl + getProblemUrl,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            Problem.class,
            identificationNumber)
        .getBody();
  }

  @Override
  public Problem updateProblem(ClientUpdateProblemRequest clientUpdateProblemRequest) {
    return restTemplate
        .exchange(
            baseUrl + updateProblemUrl,
            HttpMethod.PUT,
            new HttpEntity<>(clientUpdateProblemRequest),
            Problem.class)
        .getBody();
  }

  @Override
  public void removeProblem(String identificationNumber) {
    restTemplate.exchange(
        baseUrl + removeProblemUrl,
        HttpMethod.DELETE,
        HttpEntity.EMPTY,
        Void.class,
        identificationNumber);
  }
}
