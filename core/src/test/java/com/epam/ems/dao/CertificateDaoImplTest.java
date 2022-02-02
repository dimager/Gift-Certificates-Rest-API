package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class, loader = AnnotationConfigContextLoader.class)
class CertificateDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private CertificateDao certificateDao;

    @Test
    void getAll() {
        Assertions.assertTrue(certificateDao.getAll().size() > 480);
    }

    @Test
    void getById() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(certificateDao.getById(400)),
                () -> Assertions.assertThrows(Exception.class, () -> certificateDao.getById(-1)));
    }

    @Test
    void update() {
        long id = 160;
        Certificate certificate = certificateDao.getById(160);
        certificate.setName("newName");
        certificate.setDescription("newDescription");
        certificate.setDuration((short) 99);
        certificate.setPrice(500);

        Assertions.assertAll(() -> Assertions.assertNotEquals(certificate,certificateDao.getById(id)),
                () -> Assertions.assertEquals(certificate,certificateDao.update(certificate)));
    }

    @Test
    void create() {
        Certificate newCertificate = new Certificate();
        newCertificate.setName("testCertificate");
        newCertificate.setDescription("description");
        newCertificate.setPrice(100);
        newCertificate.setDuration((short) 2);
        Certificate certificate = certificateDao.create(newCertificate);
        Assertions.assertAll(
                () -> Assertions.assertTrue(certificate.getId() != 0));
    }

    @Test
    void delete() {
        long testCertificateId = 123;
        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(Certificate.class,certificateDao.getById(testCertificateId)),
                () -> Assertions.assertTrue(certificateDao.delete(testCertificateId)));
    }


    @Test
    void getCertificatesTags() {
        long testCertificateId = 110;
        Assertions.assertAll(
                () -> Assertions.assertFalse(certificateDao.getCertificateTags(testCertificateId).isEmpty()));
    }

    @Test
    void addTagToCertificate() {
        long testCertificateId = 110;
        long testTagId = 10;
        Tag tag = tagDao.getById(testTagId);
        Certificate certificate = certificateDao.getById(testCertificateId);

        Assertions.assertAll(
                () -> Assertions.assertTrue(certificateDao.isCertificateMissingTag(tag, certificate)),
                () -> Assertions.assertTrue(certificateDao.addTagToCertificate(tag, certificate)),
                () -> Assertions.assertFalse(certificateDao.isCertificateMissingTag(tag, certificate)));
    }

    @Test
    void removeTagFromCertificate() {
        long testCertificateId = 115;
        long testTagId = 20;
        Tag tag = tagDao.getById(testTagId);
        Certificate certificate = certificateDao.getById(testCertificateId);
        Assertions.assertAll(
                () -> Assertions.assertTrue(certificateDao.getById(testCertificateId).getTags().contains(tag)),
                () -> Assertions.assertFalse(certificateDao.isCertificateMissingTag(tag, certificate)),
                () -> Assertions.assertTrue(certificateDao.removeTagFromCertificate(tag, certificate)),
                () -> Assertions.assertFalse(certificateDao.getById(testCertificateId).getTags().contains(tag)),
                () -> Assertions.assertTrue(certificateDao.isCertificateMissingTag(tag, certificateDao.getById(testCertificateId))));
    }
}