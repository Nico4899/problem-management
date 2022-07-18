package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.connector;

import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InternalServerErrorException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.InvalidArgumentsException;
import edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception.ResourceNotFoundException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * This class represents a client error handler. It is being given to a {@link
 * org.springframework.web.client.RestTemplate} and overrides its error detection and throws service
 * internal exceptions. Which are being handled by the service later.
 */
public class RestClientErrorHandler implements ResponseErrorHandler {

  private static final int INVALID_ARGUMENTS = 400;
  private static final int RESOURCE_NOT_FOUND = 404;

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {

    return (response.getStatusCode().series() == CLIENT_ERROR
        || response.getStatusCode().series() == SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    switch (response.getStatusCode()) {
      case NOT_FOUND -> throw new ResourceNotFoundException();
      case BAD_REQUEST -> throw new InvalidArgumentsException(response.getStatusText());
      case INTERNAL_SERVER_ERROR -> throw new InternalServerErrorException(response.getStatusText());
      default -> throw new IOException(response.getStatusText());
    }
  }
}
