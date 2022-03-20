package com.epam.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
public class CertificateApplication {
    public static void main(String[] args) {
        SpringApplication.run(CertificateApplication.class, args);
    }
}
