package com.epam.ems.service.exception;


public class TagServiceException extends RuntimeException {
    public TagServiceException() {
        super();
    }

    public TagServiceException(String message) {
        super(message);
    }

    public TagServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagServiceException(Throwable cause) {
        super(cause);
    }

    protected TagServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
