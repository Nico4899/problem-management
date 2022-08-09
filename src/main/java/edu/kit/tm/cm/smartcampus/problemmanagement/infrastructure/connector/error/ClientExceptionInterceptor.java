package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector.error;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * This class represents a client error handler. It is being given to a {@link
 * RestTemplate} and overrides its error detection and throws service
 * internal exceptions. Which are being handled by the service later.
 */
public class ClientExceptionInterceptor implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return (response.getStatusCode().series() == CLIENT_ERROR
        || response.getStatusCode().series() == SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    switch (response.getStatusCode()) {
      case NOT_FOUND -> throw new NoSuchElementException();
      case BAD_REQUEST -> throw new IllegalArgumentException();
      case INTERNAL_SERVER_ERROR -> throw new InternalError();
      default -> throw new IOException();
      }
    }
  }
