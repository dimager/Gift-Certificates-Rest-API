package com.epam.ems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
    public ValidationException(HttpStatus status) {
        super(status);
    }

    public ValidationException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ValidationException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ValidationException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
