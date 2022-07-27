package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

/**
 * This exception is thrown whenever a requested resource doesn't exist, it provides a proper error
 * message.
 */
public class ResourceNotFoundException extends RuntimeException {

  private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource [%s: %s] does not exist.";

  /**
   * Constructs a {@link ResourceNotFoundException}.
   *
   * @param name name of the resource requested
   * @param value identification value of the requested resource
   */
  public ResourceNotFoundException(String name, String value) {
    super(String.format(RESOURCE_NOT_FOUND_MESSAGE, name, value));
  }

  /**
   * Constructs a {@link ResourceNotFoundException}.
   *
   * @param message error message
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
