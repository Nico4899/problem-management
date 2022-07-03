package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.logic.model.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Component
public class RestTemplateProblemConnector implements ProblemConnector{

  @Value("problem.listProblemsUrl")
  private String listProblemsUrl;
  @Value("problem.createProblemUrl")
  private String createProblemUrl;
  @Value("problem.getProblemUrl")
  private String getProblemUrl;
  @Value("problem.updateProblemUrl")
  private String updateProblemUrl;
  @Value("problem.removeProblemUrl")
  private String removeProblemUrl;

  private final String baseUrl;
  private final RestTemplate restTemplate;

  @Autowired
  public RestTemplateProblemConnector(RestTemplate restTemplate, @Value("${building.baseUrl}") String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
  }
  @Override
  public Collection<Problem> listProblems() {
    return null;
  }

  @Override
  public Problem createProblem(Problem problem) {
    return null;
  }

  @Override
  public Problem getProblem(String identificationNumber) {
    return null;
  }

  @Override
  public Problem updateProblem(Problem problem) {
    return null;
  }

  @Override
  public void removeProblem(String identificationNumber) {

  }
}
