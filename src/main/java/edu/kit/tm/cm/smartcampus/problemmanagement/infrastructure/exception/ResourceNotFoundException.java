package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final String RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE =
      "Resource not found. Maybe your request was wrong?";

  public ResourceNotFoundException() {
    super(RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE);
  }
}
