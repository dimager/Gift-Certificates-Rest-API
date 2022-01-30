package com.epam.ems.service.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.CertificateService;
import com.epam.ems.service.TagService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String MSG_CERTIFICATES_WERE_NOT_FOUND = "30401;Certificates were not found.";
    private static final String MSG_CERTIFICATE_WAS_NOT_FOUND = "30402;Certificate was not found. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_DELETED = "30403;Certificate was not deleted. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_UPDATED = "30404;Certificate was not updated. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_CREATED = "30405;Certificate was not created. Certificate name=";
    private static final String MSG_GET_CERTIFICATE_TAGS_FAIL = "30406;Cannot get certificate`s tags. Certificate id=";
    private final CertificateDao certificateDao;
    private TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Autowired
    @Override
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return getCertificates();
    }

    @Override
    public List<Certificate> getAllCertificates(boolean sorted, boolean desc) {
        if (sorted) {
            List<Certificate> sortedList = getCertificates().stream()
                    .sorted(Comparator.comparing(Certificate::getName))
                    .collect(Collectors.toList());
            if (desc) {
                Collections.sort(sortedList, Collections.reverseOrder());
            }
            return sortedList;
        } else {
            return getCertificates();
        }
    }

    private List<Certificate> getCertificates() {
        try {
            List<Certificate> certificates = certificateDao.getAll();
            if (certificates.isEmpty()) {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATES_WERE_NOT_FOUND);
            } else {
                return certificates;
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_CERTIFICATES_WERE_NOT_FOUND, e.getCause());
        }
    }

    @Override
    public Certificate getCertificate(long id) {
        try {
            return certificateDao.getById(id);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_FOUND + id, e.getCause());
        }

    }

    @Override
    @Transactional
    public boolean deleteCertificate(long id) {
        try {
            certificateDao.deleteCertificateRelations(id);
            if (certificateDao.delete(id)) {
                return true;
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_DELETED + id);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_CERTIFICATE_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    @Transactional
    public Certificate updateCertificate(Certificate certificate) {
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            this.updateTags(certificate);
            return certificateDao.update(certificate);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_CERTIFICATE_WAS_NOT_UPDATED + certificate.getId(), e.getCause());
        }

    }

    @Override
    @Transactional
    public Certificate createCertificate(Certificate certificate) {
        certificate.setCreatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            certificate = certificateDao.create(certificate);
            this.updateTags(certificate);
            return certificate;
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_CERTIFICATE_WAS_NOT_CREATED + certificate.getName(), e.getCause());
        }
    }

    @Override
    public List<Tag> getCertificateTags(long id) {
        try {
            return certificateDao.getCertificatesTags(id);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_GET_CERTIFICATE_TAGS_FAIL + id, e.getCause());
        }
    }


    @Override
    public List<Certificate> getByPartNameOrDescription(String pattern) {
        return certificateDao.getByPartNameOrDescription(pattern);
    }

    private void updateTags(Certificate certificate) {
        checkInputTags(certificate);
        addMissingTagRelations(certificate);
        removeExtraTagRelations(certificate);
    }

    private void checkInputTags(Certificate certificate) {
        List<Tag> tags = new ArrayList<>();
        tags.addAll(certificate.getTags());
        certificate.getTags().clear();
        for (Tag tag : tags) {
            tag = this.tagService.checkTag(tag);
            certificate.getTags().add(tag);
        }
    }

    private void removeExtraTagRelations(Certificate certificate) {
        List<Tag> tagsFromDB = this.getCertificateTags(certificate.getId());
        for (Tag tagFromDB : tagsFromDB) {
            if (!certificate.getTags().contains(tagFromDB)) {
                certificateDao.removeTagFromCertificate(tagFromDB, certificate);
            }
        }
    }

    private void addMissingTagRelations(Certificate certificate) {
        for (Tag tag : certificate.getTags()) {
            if (certificateDao.isCertificateMissingTag(tag, certificate)) {
                certificateDao.addTagToCertificate(tag, certificate);
            }
        }
    }
}
