package com.epam.ems.controller;

import com.epam.ems.exception.response.ExceptionWithCodeResponse;
import com.epam.ems.provider.MessageProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity errorGet(HttpServletRequest request) {
        ExceptionWithCodeResponse bodyVerification = (ExceptionWithCodeResponse) request.getAttribute("verificationException");
        ExceptionWithCodeResponse bodyTokenIsNull = (ExceptionWithCodeResponse) request.getAttribute("tokenException");
        if (Objects.nonNull(bodyVerification)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyVerification);
        } else if (Objects.nonNull(bodyTokenIsNull)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(bodyTokenIsNull);
        } else {
            ExceptionWithCodeResponse response = new ExceptionWithCodeResponse(
                    MessageProvider.getLocalizedExceptionMessage("40300"), "40300", HttpStatus.FORBIDDEN);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

    }
}
