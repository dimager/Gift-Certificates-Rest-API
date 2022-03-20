package com.epam.ems.exception;

import com.epam.ems.provider.MessageProvider;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ControllerException extends ResponseStatusException {
    @Getter
    private String message;
    @Getter
    private String code;

    public ControllerException(HttpStatus status, String code) {
        super(status);
        this.code = code;
        this.message = MessageProvider.getLocalizedExceptionMessage(code);
    }
}
