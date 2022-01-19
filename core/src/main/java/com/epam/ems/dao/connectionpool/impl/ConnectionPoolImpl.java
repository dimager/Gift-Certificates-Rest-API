package com.epam.ems.dao.connectionpool.impl;

import com.epam.ems.dao.connectionpool.ConnectionPool;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.beans.PropertyVetoException;

@Repository
@PropertySource("db.properties")
public class ConnectionPoolImpl implements ConnectionPool {
    @Value("${db.driver}")
    private String driver;

    @Value("${db.jdbcUrl}")
    private String jdbcUrl;

    @Value("${db.user}")
    private  String user;

    @Value("${db.password}")
    private String password;

    @Override
    @Bean
    @Scope("singleton")
    public ComboPooledDataSource ComboPooledDataSource(){
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setJdbcUrl(jdbcUrl);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return dataSource;
    }
}
