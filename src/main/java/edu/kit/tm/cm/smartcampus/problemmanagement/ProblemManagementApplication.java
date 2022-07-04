package edu.kit.tm.cm.smartcampus.problemmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProblemManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemManagementApplication.class, args);
    }

    @Bean
    public static RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
