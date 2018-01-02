package com.webapp.app.ws.exceptions;

public class MissingRequiredFieldsException extends RuntimeException{

    public MissingRequiredFieldsException(String message) {
        super(message);
    }
}
