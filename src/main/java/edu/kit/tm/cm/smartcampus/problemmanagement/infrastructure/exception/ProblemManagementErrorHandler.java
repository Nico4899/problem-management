package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class ProblemManagementErrorHandler implements ResponseErrorHandler {

    private static final int INVALID_REQUEST_MESSAGE_FRAMING = 400;
    // private static final int UNAUTHORIZED_ACCESS = 401;
    private static final int REQUESTED_DATA_DOESNT_EXIST = 404;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {

        // return true if error occurred
        return (
                response.getStatusCode().series() == CLIENT_ERROR
                        || response.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        // throw exception when 4XX error occurred
        if (response.getStatusCode().series() == CLIENT_ERROR) {
            if (response.getRawStatusCode() ==  REQUESTED_DATA_DOESNT_EXIST){
                throw new ResourceNotFoundException();
            }
            if (response.getRawStatusCode() == INVALID_REQUEST_MESSAGE_FRAMING) {
                throw new InvalidArgumentsException();
            }
        }
    }
}
