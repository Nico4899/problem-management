package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

public class InvalidStateChangeRequestException extends RuntimeException{

  private static final String INVALID_STATE_CHANGE_MESSAGE = "Invalid state change request operation '%s' from current state";

  public InvalidStateChangeRequestException(String operation) {
    super(String.format(INVALID_STATE_CHANGE_MESSAGE, operation));
  }
}
