package com.epam.ems.exception.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationExceptionResponse {
    private HttpStatus httpStatus;
    private String errorCode;
    private Map<String, String> errors = new HashMap<>();

    public ValidationExceptionResponse(HttpStatus httpStatus, String errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
