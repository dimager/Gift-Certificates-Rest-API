package com.epam.ems;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootConfiguration()
@ComponentScan("com.epam.ems")
@EnableAutoConfiguration
@EnableTransactionManagement
public class TestDaoConfig {


}
