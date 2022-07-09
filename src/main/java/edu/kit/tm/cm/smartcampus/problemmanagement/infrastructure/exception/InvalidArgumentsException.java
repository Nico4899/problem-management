package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

public class InvalidArgumentsException extends RuntimeException {

  private static final String INVALID_ARGUMENTS_EXCEPTION_MESSAGE =
    "Arguments are Invalid! Please check the following: %s";

  public static final String COLON = ": ";
  public static final String LEFT_PARENTHESIS = "(";
  public static final String RIGHT_PARENTHESIS = ")";
  public static final String COMMA = ", ";

  private String invalidArguments;

  public InvalidArgumentsException() {
    super();
  }

  public InvalidArgumentsException(String name, String input, String hint, boolean hasHint) {
    super();
    this.appendWrongArguments(name, input, hint, hasHint);
  }

  public void appendWrongArguments(String name, String input, String hint, boolean hasHint) {
    if (invalidArguments.isBlank()) {
      if (hasHint) {
        invalidArguments = name + COLON + input + LEFT_PARENTHESIS + hint + RIGHT_PARENTHESIS;
      } else {
        invalidArguments = name + COLON + input;
      }
    }
    if (hasHint) {
      invalidArguments +=
        invalidArguments
          + COMMA
          + name
          + COLON
          + input
          + LEFT_PARENTHESIS
          + hint
          + RIGHT_PARENTHESIS;
    } else {
      invalidArguments += invalidArguments + COMMA + name + COLON + input;
    }
  }

  @Override
  public String getMessage() {
    return String.format(INVALID_ARGUMENTS_EXCEPTION_MESSAGE, invalidArguments);
  }
}
