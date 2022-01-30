package com.epam.ems.service;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CertificateService {
    @Autowired
    void setTagService(TagService tagService);

    List<Certificate> getAllCertificates();

    List<Certificate> getAllCertificates(boolean sorted, boolean desc);

    Certificate getCertificate(long id);

    boolean deleteCertificate(long id);

    Certificate updateCertificate(Certificate certificate);

    Certificate createCertificate(Certificate certificate);

    List<Tag> getCertificateTags(long id);

    List<Certificate> getByPartNameOrDescription(String pattern);
}
