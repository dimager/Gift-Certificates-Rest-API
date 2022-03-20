package com.epam.ems.service.exception;

import com.epam.ems.provider.MessageProvider;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ServiceException extends ResponseStatusException {
    String message;
    String code;

    public ServiceException(HttpStatus status, String code, long id) {
        super(status);
        message = MessageProvider.getLocalizedExceptionMessage(code) + id;
        this.code = code;
    }

    public ServiceException(HttpStatus status, String code) {
        super(status);
        message = MessageProvider.getLocalizedExceptionMessage(code);
        this.code = code;
    }

    public ServiceException(HttpStatus status, String code, String args) {
        super(status);
        message = MessageProvider.getLocalizedExceptionMessage(code) + args;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
