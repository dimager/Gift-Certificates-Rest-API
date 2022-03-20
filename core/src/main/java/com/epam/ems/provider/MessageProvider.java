package com.epam.ems.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageProvider {
    private static final String DEFAULT_MESSAGE = "Default error message. code = ";
    private static final String DEFAULT_PROP = "Property doesnt found:";
    private static final String DEFAULT_VALIDATION_PROP = "Validation property doesnt found:";
    private static ResourceBundleMessageSource messageSource;

    @Autowired
    MessageProvider(ResourceBundleMessageSource messageSource) {
        MessageProvider.messageSource = messageSource;
    }

    public static String getLocalizedExceptionMessage(String code) {
        return messageSource.getMessage("exception." + code, null, DEFAULT_MESSAGE + code, LocaleContextHolder.getLocale());
    }

    public static String getLocalizedMessage(String propName) {
        return messageSource.getMessage(propName, null, DEFAULT_PROP + propName, LocaleContextHolder.getLocale());
    }

    public static String getLocalizedValidationMessage(String validCode) {
        return messageSource.getMessage("validation." + validCode, null, DEFAULT_VALIDATION_PROP + validCode, LocaleContextHolder.getLocale());
    }
}
