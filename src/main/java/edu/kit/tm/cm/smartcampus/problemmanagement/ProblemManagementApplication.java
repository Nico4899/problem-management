package edu.kit.tm.cm.smartcampus.problemmanagement;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.ProblemManagementErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProblemManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProblemManagementApplication.class, args);
  }

  @Bean
  public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.errorHandler(new ProblemManagementErrorHandler()).build();
  }
}
