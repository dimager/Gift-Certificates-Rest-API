package com.epam.ems.dao;

import com.epam.ems.dao.connectionpool.ConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
@ComponentScan("com.epam.ems")
public class JdbcTemplateFactory {
    final ConnectionPool connectionPool;

    @Autowired
    public JdbcTemplateFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(connectionPool.ComboPooledDataSource());
    }


}
