package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.error.exception;

/**
 * This exception is thrown whenever some internal server error is captured, it contains a proper
 * error message.
 */
public class InternalServerErrorException extends RuntimeException {

  /**
   * Constructs a new {@link InternalServerErrorException}.
   *
   * @param message error message
   */
  public InternalServerErrorException(String message) {
    super(message);
  }
}
