package com.epam.ems.dao.connectionpool;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public interface ConnectionPool {
    ComboPooledDataSource ComboPooledDataSource();
}
