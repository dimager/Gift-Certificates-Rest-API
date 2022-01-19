package com.epam.ems.dao;

import com.epam.ems.dao.entity.Certificate;

import java.util.List;

public interface CertificateDao {
    List<Certificate> getAllCertificates();
}
