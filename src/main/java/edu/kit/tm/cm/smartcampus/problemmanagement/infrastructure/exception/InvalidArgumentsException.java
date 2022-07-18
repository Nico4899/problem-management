package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

/** This exception is being thrown whenever invalid arguments are provided. */
public class InvalidArgumentsException extends RuntimeException {

  /**
   * Constructs a new invalid arguments exception.
   *
   * @param message exception message
   */
  public InvalidArgumentsException(String message) {
    super(message);
  }
}
