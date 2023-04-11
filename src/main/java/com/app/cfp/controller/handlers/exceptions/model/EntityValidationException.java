package com.app.cfp.controller.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.Set;

public class EntityValidationException extends CustomException {
    private static final String MESSAGE = "Entity could not be processed !";
    private static final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    public EntityValidationException(String resource, Set<String> errors) {
        super(MESSAGE, httpStatus, resource, errors);
    }
}

