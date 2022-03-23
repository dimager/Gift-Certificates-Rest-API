package com.epam.ems.jwt.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FailAuthenticationResponse {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public FailAuthenticationResponse(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
