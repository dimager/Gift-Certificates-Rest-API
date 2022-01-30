package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
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

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final String MSG_TAGS_WERE_NOT_FOUND = "30201;Tags were not found.";
    private static final String MSG_TAG_WAS_NOT_FOUND = "30202;Tag was not found. Tag id=";
    private static final String MSG_TAG_WAS_NOT_UPDATED = "30203;Tag was not updated. Tag id=";
    private static final String MSG_TAG_WAS_NOT_CREATED = "30204;Tag was not created. Tag name=";
    private static final String MSG_TAG_WAS_NOT_FOUND_BY_NAME = "30205;Tag was not found by name. Tag name=";
    private static final String MSG_TAG_WAS_NOT_DELETED = "30206;Tag was not deleted. Tag id=";
    private static final String MSG_IS_TAG_MISSING = "30207;Can`t check tag existence. Tag name=";
    private static final String MSG_NO_CERTIFICATES_WITH_TAG = "30208; No certificates with tag=";
    private final TagDao tagDao;
    private  CertificateService certificateService;


    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Autowired
    @Override
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public List<Tag> getAllTags() {
        try {
            List<Tag> tags = tagDao.getAll();
            if (tags.isEmpty()) {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAGS_WERE_NOT_FOUND);
            } else {
                return tags;
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAGS_WERE_NOT_FOUND, e.getCause());
        }

    }

    @Override
    public Tag getTag(long id) {
        try {
            return tagDao.getById(id);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_FOUND + id, e.getCause());
        }
    }


    @Override
    public Tag updateTag(Tag tag) {
        try {
            return tagDao.update(tag);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_UPDATED + tag.getId(), e.getCause());
        }
    }


    @Override
    public Tag createTag(Tag tag) {
        try {
            return tagDao.create(tag);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_CREATED + tag.getName(), e.getCause());
        }
    }

    @Override
    public Tag getTag(String name) {
        try {
            return tagDao.getByName(name);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_FOUND_BY_NAME + name, e.getCause());
        }
    }

    @Override
    @Transactional
    public boolean deleteTag(long id) {
        try {
            tagDao.deleteTagRelations(id);
            if (tagDao.delete(id)) {
                return true;
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_DELETED + id);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    public Tag checkTag(Tag tag) {
        try {
            return tagDao.checkTag(tag);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_IS_TAG_MISSING, e.getCause());
        }
    }

    @Override
    public List<Certificate> getTagCertificates(String name) {
        try {
            List<Certificate> certificates = tagDao.getTagCertificates(name);
            certificates.forEach(certificate -> certificate.getTags().addAll(certificateService.getCertificateTags(certificate.getId())));
            return certificates;
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_NO_CERTIFICATES_WITH_TAG + name, e.getCause());
        }
    }

}
