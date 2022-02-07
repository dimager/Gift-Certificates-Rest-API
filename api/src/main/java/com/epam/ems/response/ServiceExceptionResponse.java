package com.epam.ems.response;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class ServiceExceptionResponse {

    private String message;
    private String errorCode;
    private HttpStatus httpStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceExceptionResponse that = (ServiceExceptionResponse) o;
        return Objects.equals(message, that.message) && Objects.equals(errorCode, that.errorCode) && httpStatus == that.httpStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, errorCode, httpStatus);
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", httpStatus=" + httpStatus +
                '}';
    }
}
