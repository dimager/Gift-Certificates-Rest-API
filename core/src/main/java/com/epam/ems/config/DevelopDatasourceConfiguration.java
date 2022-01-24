package com.epam.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.ems.dao")
@Profile("dev")
public class DevelopDatasourceConfiguration implements DataSourceConfiguration{
    @Bean
    @Override
    public DataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        driverManagerDataSource.setUrl("jdbc:mysql://mager.keenetic.pro:35000/gift_develop");
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("!Pa$$w0rd");
        return driverManagerDataSource;
    }

}
