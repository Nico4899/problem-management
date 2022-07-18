package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

/**
 * This exception is being thrown whenever an internal server error occurred.
 */
public class InternalServerErrorException extends RuntimeException {
  /**
   * Constructs a new internal server error exception.
   *
   * @param message exception error message
   */
  public InternalServerErrorException(String message) {
    super(message);
  }
}
