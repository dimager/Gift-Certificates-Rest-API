package com.epam.ems.service;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;

public interface CertificateService  {
    List<Certificate> getAllCertificates();
    Certificate getCertificate(long id);
    void deleteCertificate(long id);
    Certificate updateCertificate(Certificate certificate);
    Certificate createCertificate(Certificate certificate);
    List<Tag> getCertificatesTags(long id);

}
