package com.epam.ems.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceException extends ResponseStatusException {

    public ServiceException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ServiceException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
