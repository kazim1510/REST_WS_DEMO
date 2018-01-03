package com.webapp.app.ws.ui.model.response;

public enum ErrorMessages {

    MISSING_REQUIRED_FIELD("Missing required fields. Please check documenation for required fields"),
    RECORD_ALREADY_EXIST("Record already exist"),
    NO_RECORD_FOUND("No Record Found"),
    AUTHENTICATION_FAILED("Login failed"),
    INTERNAL_SERVER_ERROR("Internal Server Error");

    private String errorMessages;

    ErrorMessages(String errorMessages){
        this.errorMessages = errorMessages;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }


}
