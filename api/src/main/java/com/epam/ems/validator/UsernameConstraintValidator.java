package com.epam.ems.validator;

import com.epam.ems.annotaion.ValidUsername;
import org.passay.AllowedRegexRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.spring.SpringMessageResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class UsernameConstraintValidator implements ConstraintValidator<ValidUsername, String> {
    private final SpringMessageResolver springMessageResolver;

    @Autowired
    public UsernameConstraintValidator(SpringMessageResolver springMessageResolver) {
        this.springMessageResolver = springMessageResolver;
    }

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        //This method is guaranteed to be called before any use of this instance for validation.
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator usernameValidator = new PasswordValidator(springMessageResolver, Arrays.asList(
                new AllowedRegexRule("^[A-Za-z0-9]*$"),
                new LengthRule(3, 45),
                new WhitespaceRule()));
        RuleResult result = usernameValidator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }
        StringBuilder validationErrors = new StringBuilder();
        usernameValidator.getMessages(result).stream().forEach(validationErrors::append);
        replaceWordPasswordToUsername(validationErrors);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationErrors.toString()).addConstraintViolation();
        return false;
    }

    private void replaceWordPasswordToUsername(StringBuilder validationErrors) {
        String wordPassword = "Password";
        String wordUsername = "Username";

        String wordPasswordRu = "Пароль";
        String wordUsernameRu = "Имя пользователя";

        int wordIndex = validationErrors.indexOf(wordPassword);
        while (wordIndex != -1) {
            validationErrors.replace(wordIndex, wordIndex + wordPassword.length(), wordUsername);
            wordIndex += wordUsername.length();
            wordIndex = validationErrors.indexOf(wordPassword, wordIndex);
        }

        int wordIndexRu = validationErrors.indexOf(wordPasswordRu);
        while (wordIndexRu != -1) {
            validationErrors.replace(wordIndexRu, wordIndexRu + wordPasswordRu.length(), wordUsernameRu);
            wordIndexRu += wordUsernameRu.length();
            wordIndexRu = validationErrors.indexOf(wordPasswordRu, wordIndexRu);
        }

    }
}
