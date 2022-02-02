package com.epam.ems.handler;

import com.epam.ems.exception.ValidationException;
import com.epam.ems.response.DefaultExceptionResponse;
import com.epam.ems.response.ServiceExceptionResponse;
import com.epam.ems.response.ValidationExceptionResponse;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Configuration
@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        DefaultExceptionResponse defaultResponse = new DefaultExceptionResponse(ex.getMessage());
        defaultResponse.setHttpStatus(status);
        return super.handleExceptionInternal(ex, defaultResponse, headers, status, request);
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceExceptionResponse> responseEntity(ServiceException e) {
        String errorMessage[] = e.getReason().split(";");
        ServiceExceptionResponse exceptionResponse = new ServiceExceptionResponse();
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
