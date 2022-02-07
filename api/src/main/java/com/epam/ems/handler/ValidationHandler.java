package com.epam.ems.handler;

import com.epam.ems.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ValidationHandler {
    public void handleBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors()
                    .forEach(fieldError -> errors.append(fieldError.getField())
                            .append(";")
                            .append(fieldError.getDefaultMessage())
                            .append("#"));
            throw new ValidationException(HttpStatus.NOT_FOUND, errors.toString());
        }
    }
}
