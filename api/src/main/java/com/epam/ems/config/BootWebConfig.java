package com.epam.ems.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = "com.epam.ems", exclude = HypermediaAutoConfiguration.class)
@EnableTransactionManagement
@EntityScan(basePackages = "com.epam.ems.entity")
public class BootWebConfig {
    public static void main(String[] args) {
        SpringApplication.run(BootWebConfig.class, args);
    }
}


