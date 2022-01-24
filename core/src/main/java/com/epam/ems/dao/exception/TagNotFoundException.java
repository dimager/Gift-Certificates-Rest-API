package com.epam.ems.dao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TagNotFoundException extends ResponseStatusException {
    public TagNotFoundException(HttpStatus status) {
        super(status);
    }

    public TagNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public TagNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public TagNotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
