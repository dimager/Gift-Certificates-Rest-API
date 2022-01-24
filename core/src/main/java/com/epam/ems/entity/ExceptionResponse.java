package com.epam.ems.entity;

public class ExceptionResponse {

    private String message;
    private long errorCode;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message, long errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }
}
