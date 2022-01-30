package com.epam.ems.service;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateDao certificateDao;

    @Mock
    private TagService tagService;

    private CertificateService certificateService;
    private List<Certificate> certificates;
    private Certificate certificate;
    private List<Certificate> certificatesEmpty = new ArrayList<>();
    private DataAccessException dataAccessException = new DataAccessException("message") {
        @Override
        public String getMessage() {
            return super.getMessage();
        }
    };

    @BeforeEach
    void setUp() {
        certificates = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Certificate certificate = new Certificate();
            certificate.setId(i);
            certificate.setName("certName" + i);
            certificate.setDescription("description" + 1);
            certificate.setPrice(i + 10);
            certificate.setDuration((short) (i * 3));
            certificate.setCreatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            for (int j = 1; j <= 5; j++) {
                certificate.getTags().add(new Tag(j, "name" + j));
            }
            certificates.add(certificate);
        }
        certificate = certificates.get(1);
        certificateService = new CertificateServiceImpl(certificateDao);
        certificateService.setTagService(tagService);
    }

    @Test
    void getAllCertificates() {
        when(certificateDao.getAll())
                .thenReturn(certificates)
                .thenReturn(certificatesEmpty)
                .thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertEquals(certificates.size(), certificateService.getAllCertificates().size()),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.getAllCertificates()),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.getAllCertificates()));
    }

    @Test
    void getCertificate() {
        when(certificateDao.getById(anyLong()))
                .thenReturn(certificate)
                .thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertEquals(certificate, certificateService.getCertificate(anyLong())),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificate(anyLong())));
    }

    @Test
    void deleteCertificate() {
        Assertions.assertAll(() -> when(certificateDao.delete(anyLong()))
                        .thenReturn(true)
                        .thenReturn(false)
                        .thenThrow(dataAccessException),
                () -> Assertions.assertTrue(certificateService.deleteCertificate(anyLong())),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.deleteCertificate(anyLong())),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.deleteCertificate(anyLong())));
    }

    @Test
    void updateCertificate() {
        when(tagService.checkTag(any(Tag.class))).thenAnswer((Answer<Tag>) invocation -> invocation.getArgument(0, Tag.class));
        when(certificateDao.isCertificateMissingTag(any(Tag.class), any(Certificate.class))).thenReturn(false);
        when(certificateDao.getCertificatesTags(anyLong())).thenReturn(certificate.getTags());
        when(certificateDao.update(any(Certificate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0,Certificate.class))
                .thenThrow(dataAccessException);

        Assertions.assertAll(
                () -> Assertions.assertEquals(certificate, certificateService.updateCertificate(certificate)),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.updateCertificate(certificate)));
    }

    @Test
    void createCertificate() {
        long generatedId = 100;
        when(tagService.checkTag(any(Tag.class))).thenAnswer((Answer<Tag>) invocation -> invocation.getArgument(0, Tag.class));
        when(certificateDao.isCertificateMissingTag(any(Tag.class), any(Certificate.class))).thenReturn(false);
        when(certificateDao.getCertificatesTags(anyLong())).thenReturn(certificate.getTags());
        when(certificateDao.create(any(Certificate.class)))
                .thenAnswer((Answer<Certificate>) invocation -> {
                            Certificate certificate = invocation.getArgument(0, Certificate.class);
                            certificate.setId(generatedId);
                            return certificate;
                })
                .thenThrow(dataAccessException);
        Certificate createdCertificate = certificateService.createCertificate(certificate);
        Assertions.assertAll(
                () -> Assertions.assertTrue(createdCertificate.getCreatedDateTime()
                        .after(Timestamp.valueOf(LocalDateTime.now().minusSeconds(5)))),
                () -> Assertions.assertTrue(createdCertificate.getLastUpdatedDateTime()
                        .after(Timestamp.valueOf(LocalDateTime.now().minusSeconds(5)))),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.createCertificate(certificate)));
    }

    @Test
    void getCertificateTags() {
        when(certificateDao.getCertificatesTags(anyLong()))
                .thenReturn(certificate.getTags())
                .thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertEquals(certificate.getTags(), certificateService.getCertificateTags(certificate.getId())),
                () -> Assertions.assertThrows(ServiceException.class, () -> certificateService.getCertificateTags(anyLong())));
    }
}































