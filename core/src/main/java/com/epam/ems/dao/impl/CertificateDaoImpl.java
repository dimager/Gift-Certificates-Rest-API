package com.epam.ems.dao.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.JdbcTemplateFactory;
import com.epam.ems.dao.entity.Certificate;
import com.epam.ems.dao.rowmapper.CertificateRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ComponentScan("com.epam.ems")
public class CertificateDaoImpl implements CertificateDao {

    private final JdbcTemplateFactory JdbcTemplateFactory;
    @Value("${sql.select_all_certificates}")
    private String sqlSelectAllCertificates;

    @Autowired
    public CertificateDaoImpl(JdbcTemplateFactory jdbcTemplateFactory) {
        JdbcTemplateFactory = jdbcTemplateFactory;
    }


    @Override
    public List<Certificate> getAllCertificates() {
            return JdbcTemplateFactory.getJdbcTemplate().query(sqlSelectAllCertificates,new CertificateRowMapper());
    }
}
