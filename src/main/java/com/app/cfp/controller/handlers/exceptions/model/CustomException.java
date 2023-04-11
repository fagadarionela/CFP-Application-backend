package com.app.cfp.controller.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class CustomException extends RuntimeException {
    private final String resource;
    private final HttpStatus status;
    private final Set<String> validationErrors;

    public CustomException(String message, HttpStatus status, String resource, Set<String> errors) {
        super(message);
        this.resource = resource;
        this.validationErrors = errors;
        this.status = status;
    }

    public String getResource() {
        return resource;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Set<String> getValidationErrors() {
        return validationErrors;
    }
}
