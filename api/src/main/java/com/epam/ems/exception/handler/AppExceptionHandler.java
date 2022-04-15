package com.epam.ems.exception.handler;

import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.exception.response.ExceptionWithCodeResponse;
import com.epam.ems.exception.response.ValidationExceptionResponse;
import com.epam.ems.provider.MessageProvider;
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

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger LOGGER = LogManager.getLogger(AppExceptionHandler.class);
    private final static String TYPE_MISMATCH_CODE = "30701";
    private final static String NO_HANDLER_CODE = "30702";
    private final static String INCORRECT_FILED_CODE = "30703";
    private final static String INTERNAL_EXCEPTION_CODE = "30704";
    private final static String VALIDATION_EXCEPTION_CODE = "30100";


    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(TYPE_MISMATCH_CODE), TYPE_MISMATCH_CODE, status);
        LOGGER.error(ex);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(NO_HANDLER_CODE), NO_HANDLER_CODE, status);
        LOGGER.error(ex);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(INCORRECT_FILED_CODE), INCORRECT_FILED_CODE, status);
        LOGGER.error(ex);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(INTERNAL_EXCEPTION_CODE), INTERNAL_EXCEPTION_CODE, status);
        LOGGER.error(ex);
        return new ResponseEntity<>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionResponse response = new ValidationExceptionResponse(status, VALIDATION_EXCEPTION_CODE);
        for (FieldError fieldError : ex.getFieldErrors()) {
            response.getErrors().put(fieldError.getField(), MessageProvider.getLocalizedValidationMessage(fieldError.getDefaultMessage()));
        }
        LOGGER.error(ex);
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ExceptionWithCodeResponse> handleIncorrectUserId(JwtAuthenticationException e) {
        ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(e.getMessage(), e.getCode(), e.getHttpStatus());
        LOGGER.error(e);
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionWithCodeResponse> handleServiceException(ServiceException e) {
        ExceptionWithCodeResponse exceptionResponse = new ExceptionWithCodeResponse(e.getMessage(), e.getCode(), e.getStatus());
        LOGGER.error(e);
        return ResponseEntity.status(e.getStatus()).body(exceptionResponse);
    }
}