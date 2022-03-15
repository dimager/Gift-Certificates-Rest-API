package com.epam.ems.exception.handler;

import com.epam.ems.exception.ControllerException;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.exception.response.DefaultExceptionResponse;
import com.epam.ems.exception.response.ExceptionWithCodeResponse;
import com.epam.ems.exception.response.ValidationExceptionResponse;
import com.epam.ems.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    Logger logger = LogManager.getLogger(AppExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DefaultExceptionResponse defaultExceptionResponse = new DefaultExceptionResponse();
        defaultExceptionResponse.setMessage(ex.getErrorCode() + ": " + ex.getValue());
        defaultExceptionResponse.setHttpStatus(status);
        logger.error(ex);
        return new ResponseEntity<>(defaultExceptionResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionResponse response = new ValidationExceptionResponse();
        for (FieldError fieldError : ex.getFieldErrors()) {
            response.getErrors().put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        response.setHttpStatus(status);
        response.setErrorCode("30100");
        logger.error(ex);
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionWithCodeResponse> handleServiceException(ServiceException e) {
        ExceptionWithCodeResponse exceptionResponse = new ExceptionWithCodeResponse(e.getReason(),e.getStatus());
        logger.error(e);
        return ResponseEntity.status(e.getStatus()).body(exceptionResponse);
    }

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ExceptionWithCodeResponse> handleControllerException(ControllerException e) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(e.getReason(),e.getStatus());
        logger.error(e);
        return ResponseEntity.status(e.getStatus()).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DefaultExceptionResponse defaultExceptionResponse = new DefaultExceptionResponse();
        defaultExceptionResponse.setMessage(ex.getMessage());
        defaultExceptionResponse.setHttpStatus(status);
        logger.error(ex);
        return new ResponseEntity<>(defaultExceptionResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DefaultExceptionResponse defaultExceptionResponse = new DefaultExceptionResponse();
        defaultExceptionResponse.setMessage("Incorrect field in body request");
        defaultExceptionResponse.setHttpStatus(status);
        logger.error(ex);
        return new ResponseEntity<>(defaultExceptionResponse, headers, status);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DefaultExceptionResponse defaultExceptionResponse = new DefaultExceptionResponse();
        defaultExceptionResponse.setMessage("handleExceptionInternal");
        defaultExceptionResponse.setHttpStatus(status);
        logger.error(ex);
        return new ResponseEntity<>(defaultExceptionResponse, headers, status);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<DefaultExceptionResponse> handleIncorectUserId(JwtAuthenticationException e) {
        DefaultExceptionResponse defaultExceptionResponse = new DefaultExceptionResponse();
        defaultExceptionResponse.setMessage(e.getMessage());
        defaultExceptionResponse.setHttpStatus(e.getHttpStatus());
        logger.error(e);
        return ResponseEntity.status(e.getHttpStatus()).body(defaultExceptionResponse);
    }
}