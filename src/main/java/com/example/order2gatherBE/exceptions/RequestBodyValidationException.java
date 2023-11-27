package com.example.order2gatherBE.exceptions;

public class RequestBodyValidationException extends RuntimeException {

    public RequestBodyValidationException(String message) {
        super(message);
    }
}
