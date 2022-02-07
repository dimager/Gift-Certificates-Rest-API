package com.epam.ems;

import com.epam.ems.dao.impl.CertificateDaoImpl;
import com.epam.ems.dao.impl.TagDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Import({TagDaoImpl.class, CertificateDaoImpl.class})
public class TestDaoConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql")
                .addScript("data.sql")
                .build();
    }
}
