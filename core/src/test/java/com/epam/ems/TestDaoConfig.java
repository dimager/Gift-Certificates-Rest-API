package com.epam.ems;

import com.epam.ems.config.ResourceConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;


@Import({ResourceConfiguration.class})
@EntityScan("com.epam.ems.entity")
@EnableAutoConfiguration
public class TestDaoConfig {

}
