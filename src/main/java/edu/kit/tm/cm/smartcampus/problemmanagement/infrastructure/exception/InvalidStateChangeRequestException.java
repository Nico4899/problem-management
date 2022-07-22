package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

/** Exception thrown whenever an invalid state change request is sent. */
public class InvalidStateChangeRequestException extends RuntimeException {

  private static final String INVALID_STATE_CHANGE_MESSAGE =
      "Invalid state change request operation '%s' from current state '%s'.";

  /**
   * Constructs a new invalid state change exception.
   *
   * @param operation operation which is being tried to execute
   */
  public InvalidStateChangeRequestException(String operation, String state) {
    super(String.format(INVALID_STATE_CHANGE_MESSAGE, operation, state));
  }
}
