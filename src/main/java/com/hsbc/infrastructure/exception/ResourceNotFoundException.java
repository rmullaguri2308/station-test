package com.hsbc.infrastructure.exception;

/**
 * Exception to indicate an error when resource not found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
