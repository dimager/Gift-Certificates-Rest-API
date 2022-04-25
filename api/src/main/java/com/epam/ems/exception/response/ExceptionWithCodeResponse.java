package com.epam.ems.exception.response;

import lombok.Data;
import lombok.Generated;
import org.springframework.http.HttpStatus;

import java.util.Objects;

@Data
public class ExceptionWithCodeResponse {

    private String message;
    private String errorCode;
    private HttpStatus httpStatus;

    public ExceptionWithCodeResponse(String message, String code, HttpStatus status) {
        this.httpStatus = status;
        this.errorCode = code;
        this.message = message;
    }


    @Override
    @Generated
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionWithCodeResponse that = (ExceptionWithCodeResponse) o;
        return Objects.equals(message, that.message) && Objects.equals(errorCode, that.errorCode) && httpStatus == that.httpStatus;
    }

    @Override
    @Generated
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
