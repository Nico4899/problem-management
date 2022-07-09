package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class RestClientErrorHandler implements ResponseErrorHandler {

  private static final int INVALID_ARGUMENTS = 400;
  private static final int REQUESTED_DATA_DOESNT_EXIST = 404;

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {

    return (response.getStatusCode().series() == CLIENT_ERROR
        || response.getStatusCode().series() == SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {

    if (response.getStatusCode().series() == CLIENT_ERROR) {
      if (response.getRawStatusCode() == REQUESTED_DATA_DOESNT_EXIST) {
        throw new ResourceNotFoundException();
      }
      if (response.getRawStatusCode() == INVALID_ARGUMENTS) {
        throw new InvalidArgumentsException();
      }
    }
  }
}
