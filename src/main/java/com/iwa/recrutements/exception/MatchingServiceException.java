package com.iwa.recrutements.exception;

public class MatchingServiceException extends RuntimeException {

    public MatchingServiceException(String message) {
        super(message);
    }

    public MatchingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
