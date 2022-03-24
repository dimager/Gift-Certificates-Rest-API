package com.epam.ems.exception;

import com.epam.ems.provider.MessageProvider;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String code;

    @Getter
    private String message;

    public JwtAuthenticationException(String code, HttpStatus httpStatus) {
        super(code);
        this.code = code;
        this.message = MessageProvider.getLocalizedExceptionMessage(code);
        this.httpStatus = httpStatus;
    }
}
