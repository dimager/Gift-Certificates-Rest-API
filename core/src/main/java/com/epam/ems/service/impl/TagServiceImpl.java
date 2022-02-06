package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
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
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }


    @Override
    public List<Tag> getAllTags() {
        try {
            return tagDao.getAll();
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAGS_WERE_NOT_FOUND, e.getCause());
        }

    }


    @Override
    public Tag getTag(long id) {
        try {
            return tagDao.getById(id);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_FOUND + id, e.getCause());
        }
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        try {
            return tagDao.update(tag);
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_UPDATED + tag.getId(), e.getCause());
        }
    }


    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        try {
            if (tagDao.isTagExistByName(tag.getName())) {
                return tagDao.getByName(tag.getName());
            } else {
                return tagDao.create(tag);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_CREATED + tag.getName(), e.getCause());
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
            if (tagDao.isTagExistById(id)) {
                return tagDao.delete(id);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_FOUND + id);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    public boolean isTagExistByName(String name) {
        return tagDao.isTagExistByName(name);
    }
}
