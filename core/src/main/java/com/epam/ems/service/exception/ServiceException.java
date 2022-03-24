package com.epam.ems.service.exception;

import com.epam.ems.provider.MessageProvider;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class ServiceException extends ResponseStatusException {
    private String message;
    private String code;
    private Long id;
    private String args = "";

    public ServiceException(HttpStatus status, String code, long id) {
        super(status);
        message = MessageProvider.getLocalizedExceptionMessage(code) + id;
        this.id = id;
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
        this.args = args;
        this.code = code;
    }

}
