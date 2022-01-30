package com.epam.ems.handler;

import com.epam.ems.entity.ExceptionResponse;
import com.epam.ems.entity.ValidationExceptionResponse;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.exception.ValidationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Configuration
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> responseEntity(ServiceException e) {
        String errorMessage[] = e.getReason().split(";");
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setHttpStatus(e.getStatus());
        exceptionResponse.setErrorCode(errorMessage[0]);
        exceptionResponse.setMessage(errorMessage[1]);
        return ResponseEntity.status(e.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationExceptionResponse> validationEntity(ValidationException e) {
        ValidationExceptionResponse response = new ValidationExceptionResponse();
        response.setHttpStatus(e.getStatus());
        response.setErrorCode("30100");
        for (String fieldError : e.getReason().split("#")) {
            String[] splitError = fieldError.split(";");
            response.getErrors().put(splitError[0], splitError[1]);
        }
        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
