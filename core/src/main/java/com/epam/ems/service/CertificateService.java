package com.epam.ems.service;

import com.epam.ems.dao.entity.Certificate;

import java.util.List;

public interface CertificateService  {
    List<Certificate> getAllCertificates();
}
