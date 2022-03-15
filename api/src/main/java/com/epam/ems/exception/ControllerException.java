package com.epam.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ControllerException extends ResponseStatusException {
    public ControllerException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
