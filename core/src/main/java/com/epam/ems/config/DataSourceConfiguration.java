package com.epam.ems.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.ems.dao")
@PropertySource("classpath:db.properties")
public class DataSourceConfiguration {
    @Value("${dev.driver}")
    private String devDriver;
    @Value("${dev.jdbcUrl}")
    private String devUrl;
    @Value("${dev.user}")
    private String devUser;
    @Value("${dev.password}")
    private String devPassword;
    @Value("${prod.driver}")
    private String prodDriver;
    @Value("${prod.jdbcUrl}")
    private String prodUrl;
    @Value("${prod.user}")
    private String prodUser;
    @Value("${prod.password}")
    private String prodPassword;

    @Bean
    @Profile("dev")
    public DataSource developDataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(devDriver);
        driverManagerDataSource.setUrl(devUrl);
        driverManagerDataSource.setUsername(devUser);
        driverManagerDataSource.setPassword(devPassword);
        return driverManagerDataSource;
    }

    @Bean
    @Profile("!dev")
    public DataSource productionDataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(prodDriver);
        driverManagerDataSource.setUrl(prodUrl);
        driverManagerDataSource.setUsername(prodUser);
        driverManagerDataSource.setPassword(prodPassword);
        return driverManagerDataSource;
    }

}
