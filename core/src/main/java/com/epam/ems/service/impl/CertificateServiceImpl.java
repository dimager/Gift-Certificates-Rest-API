package com.epam.ems.service.impl;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.CertificateService;
import com.epam.ems.service.PageService;
import com.epam.ems.service.TagService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CertificateServiceImpl implements CertificateService {
    private static final String MSG_CERTIFICATES_WERE_NOT_FOUND = "30401;Certificates were not found.";
    private static final String MSG_CERTIFICATE_WAS_NOT_FOUND = "30402;Certificate was not found. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_DELETED = "30403;Certificate was not deleted. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_UPDATED = "30404;Certificate was not updated. Certificate id=";
    private static final String MSG_CERTIFICATE_WAS_NOT_CREATED = "30405;Certificate was not created. Certificate name=";
    private final CertificateDao certificateDao;
    private final TagService tagService;
    private final PageService pageService;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagService tagService, PageService pageService) {
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.pageService = pageService;
    }

    @Override
    @Transactional
    public CollectionModel<Certificate> getFilteredSortedCertificates(Optional<String> sort,
                                                                      Optional<String[]> tagsNames,
                                                                      Optional<String> filterPattern,
                                                                      int page,
                                                                      int size,
                                                                      WebMvcLinkBuilder link) {
        try {
            List<Certificate> certificates;
            List<Link> links;
            HashMap<String, String> extraParams = new HashMap<>();
            long totalSize;
            int offset = pageService.getOffset(size, page);
            totalSize = certificateDao.getCertificatesAmount(sort, filterPattern);
            certificates = certificateDao.getCertificates(size, offset, sort, filterPattern);
            sort.ifPresent(s -> extraParams.put("sort", s));
            filterPattern.ifPresent(s -> extraParams.put("filter", s));

            if (tagsNames.isPresent()) {
                Set<Tag> tags = new LinkedHashSet<>();
                for (String s : tagsNames.get()) {
                    tags.add(tagService.getTag(s));
                }
                extraParams.put("tags", String.join(",", tagsNames.get()));
                totalSize = certificateDao.getNumberOCertificatesContainsTags(tags);
                certificates = certificateDao.getCertificatesContainsTags(size, offset, tags);
            }

            links = pageService.createLinksWithStringParameters(size, page, totalSize, link, extraParams);
            pageService.isPageExist(page, totalSize, offset);
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, totalSize);
            return PagedModel.of(certificates, metadata, links);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATES_WERE_NOT_FOUND, e.getCause());
        }
    }

    @Override
    public Certificate getCertificate(long id) {
        try {
            if (certificateDao.isCertificateExistById(id)) {
                return certificateDao.getById(id);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_FOUND + id);
            }
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    @Transactional
    public Certificate updateCertificate(Certificate certificate) {
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            if (certificateDao.isCertificateExistById(certificate.getId())) {
                certificate.setCreatedDateTime(certificateDao.getById(certificate.getId()).getCreatedDateTime());
                this.checkInputTagsForExistenceInDatabase(certificate);
                return certificateDao.update(certificate);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_UPDATED + certificate.getId());
            }
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_UPDATED + certificate.getId(), e.getCause());
        }

    }

    @Override
    @Transactional
    public Certificate createCertificate(Certificate certificate) {
        certificate.setCreatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
        try {
            this.checkInputTagsForExistenceInDatabase(certificate);
            certificate = certificateDao.create(certificate);
            return certificate;
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_CREATED + certificate.getName(), e.getCause());
        }
    }

    @Override
    @Transactional
    public boolean updateDuration(long id, Certificate durationOnly) {
        try {
            if (certificateDao.isCertificateExistById(id)) {
                Certificate certificate = certificateDao.getById(id);
                certificate.setDuration(durationOnly.getDuration());
                certificate.setLastUpdatedDateTime(Timestamp.valueOf(LocalDateTime.now()));
                certificateDao.update(certificate);
                return certificateDao.getById(id).getDuration() == durationOnly.getDuration();
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_FOUND + id);
            }
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_CERTIFICATE_WAS_NOT_UPDATED + id, e.getCause());
        }
    }


    private void checkInputTagsForExistenceInDatabase(Certificate certificate) {
        List<Tag> tags = new ArrayList<>(certificate.getTags());
        certificate.getTags().clear();
        for (Tag tag : tags) {
            if (tagService.isTagExistByName(tag.getName())) {
                certificate.getTags().add(tagService.getTag(tag.getName()));
            } else {
                certificate.getTags().add(tagService.createTag(tag));
            }
        }
    }
}
