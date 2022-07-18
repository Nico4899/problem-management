package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

/** Exception thrown whenever a resource was not found. */
public class ResourceNotFoundException extends RuntimeException {

  private static final String RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE =
      "Resource not found. Maybe your request was wrong?";

  /** Constructs a new resource not found exception. */
  public ResourceNotFoundException() {
    super(RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE);
  }
}
