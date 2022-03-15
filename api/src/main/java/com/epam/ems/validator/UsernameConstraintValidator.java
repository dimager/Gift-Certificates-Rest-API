package com.epam.ems.validator;

import com.epam.ems.annotaion.ValidUsername;
import org.passay.AllowedRegexRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UsernameConstraintValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator usernameValidator = new PasswordValidator(Arrays.asList(
                new AllowedRegexRule("^[A-Za-z0-9]*$"),
                new LengthRule(3, 30),
                new WhitespaceRule()));
        RuleResult result = usernameValidator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }
        StringBuilder validationErrors = new StringBuilder();
        usernameValidator.getMessages(result).stream().forEach(message -> validationErrors.append(message));
        replaceWordPasswordToUsername(validationErrors);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationErrors.toString()).addConstraintViolation();
        return false;
    }

    private void replaceWordPasswordToUsername(StringBuilder validationErrors) {
        String wordPassword = "Password";
        String wordUsername = "Username";

        int wordIndex = validationErrors.indexOf(wordPassword);
        while (wordIndex != -1) {
            validationErrors.replace(wordIndex, wordIndex + wordPassword.length(), wordUsername);
            wordIndex += wordUsername.length();
            wordIndex = validationErrors.indexOf(wordPassword, wordIndex);
        }
    }
}
