package com.epam.ems.validator;

import com.epam.ems.annotaion.ValidPassword;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
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

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private final SpringMessageResolver springMessageResolver;

    @Autowired
    public PasswordConstraintValidator(SpringMessageResolver springMessageResolver) {
        this.springMessageResolver = springMessageResolver;
    }

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(springMessageResolver,Arrays.asList(
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new LengthRule(8, 30),
                new WhitespaceRule()));

        RuleResult result = passwordValidator.validate(new PasswordData(value));
        if (result.isValid()) {
            return true;
        }
        StringBuilder validationErrors = new StringBuilder();
        passwordValidator.getMessages(result).stream().forEach(validationErrors::append);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(validationErrors.toString()).addConstraintViolation();
        return false;
    }
}
