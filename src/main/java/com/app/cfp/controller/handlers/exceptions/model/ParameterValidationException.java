package com.app.cfp.controller.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class ParameterValidationException extends CustomException {
    private static final String MESSAGE = "Parameter is invalid!";
    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ParameterValidationException(String resource, Set<String> errors) {
        super(MESSAGE, httpStatus, resource, errors);
    }
}