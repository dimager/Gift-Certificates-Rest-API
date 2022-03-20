package com.epam.ems.config;

import org.passay.spring.SpringMessageResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class BootWebConfig extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    private final MessageSource messageSource;

    @Autowired
    public BootWebConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    public SpringMessageResolver springMessageResolver() {
        return new SpringMessageResolver(messageSource);
    }

}


