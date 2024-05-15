package com.example.backend.exceptions;

public class DuplicateClientException extends RuntimeException {
    private final String errorCode;
    public DuplicateClientException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return this.errorCode;
    }
}
