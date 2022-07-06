package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

public class InvalidArgumentsException extends RuntimeException{

    private static final String INVALID_ARGUMENTS_EXCEPTION_MESSAGE =
            "Arguments are invalid for %s request. Please check and retry! ";

    public InvalidArgumentsException() {
        super(INVALID_ARGUMENTS_EXCEPTION_MESSAGE);
    }
}
