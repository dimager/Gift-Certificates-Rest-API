package com.epam.ems.jwt.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FailAuthenticationResponse {
    private HttpStatus httpStatus;
    private int code;
    private String message;

    public FailAuthenticationResponse(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
