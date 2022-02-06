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
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final String MSG_CERTIFICATES_WERE_NOT_FOUND = "30401;Certificates were not found.";
    private static final String MSG_CERTIFICATE_WAS_NOT_FOUND = "30402;Certificate was not found. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_DELETED = "30403;Certificate was not deleted. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_UPDATED = "30404;Certificate was not updated. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_CREATED = "30405;Certificate was not created. Certificate name=";
    private static final String MSG_GET_CERTIFICATE_TAGS_FAIL = "30406;Cannot get certificate`s tags. Certificate id=";
    private static final String MSG_TAG_WAS_NOT_FOUND = "30202;Tag was not found. Tag name=";

    private final static String SORT_NAME = "name";
    private final static String SORT_NAME_DESC = "name_desc";
    private final static String SORT_DATE = "date";
    private final static String SORT_DATE_DESC = "date_desc";
    private final static String SORT_NAME_DATE = "name_date";
    private final static String SORT_NAME_DATE_DESC = "name_date_desc";


    private final CertificateDao certificateDao;
    private TagService tagService;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagService tagService) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
    }


    @Override
    @Transactional
    public List<Certificate> getFilteredSortedCertificates(Optional<String> sort,
                                                           Optional<String> tagName,
                                                           Optional<String> filterPattern) {

        List<Certificate> certificates = this.getCertificates();

        if (filterPattern.isPresent()) {
            certificates = filterCertificateNameOrDescriptionContains(certificates, filterPattern.get());
        }

        if (tagName.isPresent()) {
            if (tagService.isTagExistByName(tagName.get())) {
                certificates = findCertificatesWithTag(certificates, tagName.get());
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_FOUND + tagName.get());
            }
        }

        if (sort.isPresent()) {
            switch (sort.get().toLowerCase(Locale.ROOT)) {
                case SORT_NAME:
                    sortCertificateListByName(certificates);
                    break;
                case SORT_NAME_DESC:
                    sortCertificateListByName(certificates);
                    certificates.sort(Collections.reverseOrder());
                    break;
                case SORT_DATE:
                    sortCertificateListByDate(certificates);
                    break;
                case SORT_DATE_DESC:
                    sortCertificateListByDate(certificates);
                    certificates.sort(Collections.reverseOrder());
                    break;
                case SORT_NAME_DATE:
                    sortCertificateListByNameAndDate(certificates);
                    break;
                case SORT_NAME_DATE_DESC:
                    sortCertificateListByNameAndDate(certificates);
                    certificates.sort(Collections.reverseOrder());
                    break;
                default:
                    return certificates;
            }
        }
        return certificates;
    }

    private List<Certificate> findCertificatesWithTag(List<Certificate> certificates, String tagName) {
        Tag tag = tagService.getTag(tagName);
        certificates = certificates.stream()
                .filter(certificate -> certificate.getTags()
                        .contains(tag))
                .collect(Collectors.toList());
        return certificates;
    }

    private List<Certificate> filterCertificateNameOrDescriptionContains(List<Certificate> certificates, String filterPattern) {
        certificates = certificates.stream().filter(certificate -> certificate.getName().contains(filterPattern)
                        || certificate.getDescription().contains(filterPattern))
                .collect(Collectors.toList());
        return certificates;
    }

    private void sortCertificateListByName(List<Certificate> certificates) {
        certificates.sort(Comparator.comparing(Certificate::getName));
    }

    private void sortCertificateListByDate(List<Certificate> certificates) {
        certificates.sort(Comparator.comparing(Certificate::getCreatedDateTime));
    }

    private void sortCertificateListByNameAndDate(List<Certificate> certificates) {
        certificates.sort(Comparator.comparing(Certificate::getName)
                .thenComparing(Certificate::getCreatedDateTime));
    }


    private List<Certificate> getCertificates() {
        try {
            return certificateDao.getAll();
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATES_WERE_NOT_FOUND, e.getCause());
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
            if (certificateDao.isCertificateExistById(id)) {
                return certificateDao.delete(id);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_FOUND + id);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    @Transactional
    public Certificate updateCertificate(Certificate certificate) {
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            this.updateCertificateTags(certificate);
            return certificateDao.update(certificate);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_UPDATED + certificate.getId(), e.getCause());
        }

    }

    @Override
    @Transactional
    public Certificate createCertificate(Certificate certificate) {
        certificate.setCreatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            certificate = certificateDao.create(certificate);
            this.updateCertificateTags(certificate);
            return certificate;
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_CREATED + certificate.getName(), e.getCause());
        }
    }

    @Override
    public List<Tag> getCertificateTags(long id) {
        try {
            return certificateDao.getCertificateTags(id);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_GET_CERTIFICATE_TAGS_FAIL + id, e.getCause());
        }
    }

    private void updateCertificateTags(Certificate certificate) {
        checkInputTagsForExistenceInDatabase(certificate);
        addMissingTagRelations(certificate);
        removeExtraTagRelations(certificate);
    }

    private void checkInputTagsForExistenceInDatabase(Certificate certificate) {
        List<Tag> tags = new ArrayList<>(certificate.getTags());
        certificate.getTags().clear();
        for (Tag tag : tags) {
            tag = tagService.createTag(tag);
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
