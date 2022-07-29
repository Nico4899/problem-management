package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.service.exception;

/**
 * This exception is thrown whenever invalid arguments are found, it contains a proper error
 * message.
 */
public class InvalidArgumentsException extends RuntimeException {

  /**
   * Constructs a new {@link InvalidArgumentsException}.
   *
   * @param message the message provided
   */
  public InvalidArgumentsException(String message) {
    super(message);
  }
}
