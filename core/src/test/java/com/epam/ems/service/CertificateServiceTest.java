package com.epam.ems.service;


import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.CertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
class CertificateServiceTest {


    @Mock
    private CertificateDao certificateDao;

    @Mock
    private TagService tagService;

    private CertificateService certificateService;
    private List<Certificate> certificates;
    private Certificate certificate;
    @Mock
    private PageService pageService;


    @BeforeEach
    void setUp() {
        certificates = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Certificate certificate = new Certificate();
            certificate.setId(i);
            certificate.setName("certName" + i);
            certificate.setDescription("description" + 1);
            certificate.setPrice(new BigDecimal(i + 10));
            certificate.setDuration((short) (i * 3));
            certificate.setCreatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
            for (int j = 1; j <= 5; j++) {
                Tag t = new Tag();
                t.setId(j);
                t.setName("name" + j);
                certificate.getTags().add(t);
            }
            certificates.add(certificate);
        }
        certificate = certificates.get(1);
        certificateService = new CertificateServiceImpl(certificateDao, tagService, pageService);
    }

    @Test
    void updateDuration() {
        when(certificateDao.isCertificateExistById(anyLong())).thenReturn(true);
        when(certificateDao.getById(anyLong())).thenReturn(certificate);
        when(certificateDao.update(any())).thenReturn(certificate);

        Certificate certificateDuration = new Certificate();
        certificate.setDuration((short) 100);

        assertAll(() -> assertTrue(certificateService.updateDuration(anyLong(), certificateDuration)),
                () -> assertEquals(certificateDuration.getDuration(), certificate.getDuration()));
    }


    @Test
    void getCertificate() {
        when(certificateDao.isCertificateExistById(anyLong())).thenReturn(true);
        when(certificateDao.getById(anyLong()))
                .thenReturn(certificate)
                .thenThrow(ServiceException.class);
        assertAll(
                () -> assertEquals(certificate, certificateService.getCertificate(anyLong())),
                () -> assertThrows(ServiceException.class, () -> certificateService.getCertificate(anyLong())));
    }

    @Test
    void deleteCertificate() {
        when(certificateDao.isCertificateExistById(anyLong())).thenReturn(true).thenReturn(false);
        when(certificateDao.delete(anyLong()))
                .thenReturn(true)
                .thenReturn(false);
        assertAll(
                () -> assertTrue(certificateService.deleteCertificate(anyLong())),
                () -> assertThrows(ServiceException.class, () -> certificateService.deleteCertificate(anyLong())));
    }

    @Test
    void updateCertificate() {

        when(certificateDao.update(any())).thenReturn(certificate);
        when(tagService.createTag(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(certificateDao.isCertificateExistById(anyLong())).thenReturn(true).thenReturn(false);
        when(certificateDao.getById(anyLong())).thenReturn(certificate);

        assertAll(
                () -> assertEquals(certificate, certificateService.updateCertificate(certificate)),
                () -> assertThrows(ServiceException.class, () -> certificateService.updateCertificate(certificate)));
    }

    @Test
    void createCertificate() {
        when(certificateDao.create(certificate))
                .thenReturn(certificate);

        Certificate createdCertificate = certificateService.createCertificate(certificate);
        assertAll(
                () -> assertTrue(createdCertificate.getCreatedDateTime()
                        .after(Timestamp.valueOf(LocalDateTime.now().minusSeconds(5)))),
                () -> assertTrue(createdCertificate.getLastUpdatedDateTime()
                        .after(Timestamp.valueOf(LocalDateTime.now().minusSeconds(5)))));
    }


}