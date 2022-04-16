package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.Certificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestDaoConfig.class})
@ActiveProfiles("dev")
class CertificateDaoImplTest {

    @Autowired
    private CertificateDao certificateDao;

    @Test
    void getAll() {
        int pageSize = 15;
        assertEquals(pageSize, certificateDao.getAll(pageSize, 0).size());
    }


    @Test
    void getById() {
        assertAll(
                () -> assertNotNull(certificateDao.getById(123)),
                () -> assertThrows(Exception.class, () -> certificateDao.getById(-1)));
    }

    @Test
    void isCertificateExistById() {
        assertAll(
                () -> assertTrue(certificateDao.isCertificateExistById(1)),
                () -> assertFalse(certificateDao.isCertificateExistById(-1)));
    }

    @Test
    void getCertificates() {
        String filterPatter = "111";
        List<Certificate> list = certificateDao.getCertificates(100, 0, Optional.empty(), Optional.of(filterPatter));
        assertAll(() -> assertTrue(certificateDao.getCertificatesAmount(Optional.empty(), Optional.of(filterPatter)) > 0),
                () -> assertEquals(
                        list.size(),
                        list.stream()
                                .filter(certificate -> certificate.getDescription().contains(filterPatter) || certificate.getName().contains(filterPatter))
                                .collect(Collectors.toList())
                                .size()));
        ;
    }

    @Test
    @Transactional
    void update() {
        long id = 161;
        String newName = "updatedName";
        String newDescription = "updatedDescription";
        Certificate certificate = certificateDao.getById(id);
        assertAll(() -> Assertions.assertNotEquals(newName, certificate.getName()),
                () -> Assertions.assertNotEquals(newDescription, certificate.getDescription()));
        certificate.setName(newName);
        certificate.setDescription(newDescription);
        certificate.setDuration((short) 99);
        certificate.setPrice(new BigDecimal("50.02"));
        certificateDao.update(certificate);
        Certificate finalCertificate = certificateDao.getById(id);
        assertAll(() -> assertEquals(finalCertificate.getName(), newName),
                () -> assertEquals(finalCertificate.getDescription(), newDescription));
    }

    @Test
    @Transactional
    void create() {
        Certificate newCertificate = new Certificate();
        newCertificate.setName("testCertificate");
        newCertificate.setDescription("description");
        newCertificate.setPrice(new BigDecimal(100));
        newCertificate.setDuration((short) 2);
        Certificate certificate = certificateDao.create(newCertificate);
        Certificate certFroDB = certificateDao.getById(certificate.getId());
        assertAll(
                () -> assertNotEquals(0, certificate.getId()),
                () -> assertEquals(certificate, certFroDB));
    }

    @Test
    @Transactional
    void delete() {
        long testCertificateId = 123;
        assertAll(
                () -> Assertions.assertInstanceOf(Certificate.class, certificateDao.getById(testCertificateId)),
                () -> assertTrue(certificateDao.delete(testCertificateId)));
    }

}