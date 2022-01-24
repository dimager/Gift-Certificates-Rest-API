package com.epam.ems.controller;

import com.epam.ems.dao.exception.TagNotFoundException;
import com.epam.ems.entity.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ExceptionResponse> responseEntity(TagNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getReason(), 10022);
        return new ResponseEntity<>(exceptionResponse, e.getStatus());
    }

}
