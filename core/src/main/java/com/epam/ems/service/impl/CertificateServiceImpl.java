package com.epam.ems.service.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CertificateServiceImpl implements CertificateService {

    private final CertificateDao certificateDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }


    @Override
    public List<Certificate> getAllCertificates() {
        List<Certificate> certificates = certificateDao.getAll();
        certificates.forEach(certificate -> certificate.getTags().addAll(getCertificatesTags(certificate.getId())));
        return certificates;
    }

    @Override
    public Certificate getCertificate(long id) {
        return certificateDao.getById(id);
    }

    @Override
    public void deleteCertificate(long id) {
        certificateDao.delete(id);
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        certificate.setLastUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
        return certificateDao.update(certificate);
    }

    @Override
    public Certificate createCertificate(Certificate certificate) {
        return certificateDao.create(certificate);
    }

    @Override
    public List<Tag> getCertificatesTags(long id) {
        return certificateDao.getCertificatesTags(id);
    }
}
