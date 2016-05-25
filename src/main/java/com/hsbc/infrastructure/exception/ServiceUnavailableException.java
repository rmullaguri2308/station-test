package com.hsbc.infrastructure.exception;

/**
 * Exception to indicate an error when service is unavailable
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
