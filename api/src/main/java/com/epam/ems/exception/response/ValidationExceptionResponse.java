package com.epam.ems.exception.response;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ValidationExceptionResponse {
    private HttpStatus httpStatus;
    private String errorCode;
    private Map<String, String> errors = new HashMap();

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
