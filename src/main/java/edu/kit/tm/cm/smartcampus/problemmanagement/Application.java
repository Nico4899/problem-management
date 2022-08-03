package edu.kit.tm.cm.smartcampus.problemmanagement;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.error.ClientExceptionInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** The main entry point of the Application. */
@SpringBootApplication
public class Application {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * The {@link RestTemplate} {@link Bean} used for the connectors.
   *
   * @return the rest template
   */
  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new ClientExceptionInterceptor());
    return restTemplate;
  }
}
