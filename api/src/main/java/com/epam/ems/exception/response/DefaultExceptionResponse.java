package com.epam.ems.exception.response;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class DefaultExceptionResponse {
    private String message;
    private HttpStatus httpStatus;

    public DefaultExceptionResponse() {
    }

    public DefaultExceptionResponse(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultExceptionResponse that = (DefaultExceptionResponse) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
