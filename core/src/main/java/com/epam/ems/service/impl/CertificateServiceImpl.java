package com.epam.ems.service.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.entity.Certificate;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ComponentScan("com.epam.ems")
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }


    @Override
    public List<Certificate> getAllCertificates() {
        return certificateDao.getAllCertificates();
    }
}
