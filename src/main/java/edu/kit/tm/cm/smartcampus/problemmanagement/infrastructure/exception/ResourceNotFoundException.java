package edu.kit.tm.cm.smartcampus.problemmanagement.infrastructure.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE =
            "Resource to identification number: %s not found. Please check and retry!";

    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
