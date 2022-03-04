package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.PageService;
import com.epam.ems.service.TagService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    private static final String MSG_TAG_EXIST = "30207;Tag exist. name=";
    private static final String MSG_WITH_NAME_EXIST = "30209;Tag with name exist. name=";
    private final TagDao tagDao;
    private final PageService pageService;

    @Autowired
    public TagServiceImpl(TagDao tagDao, PageService pageService) {
        this.tagDao = tagDao;
        this.pageService = pageService;
    }

    @Override
    @Transactional
    public CollectionModel<Tag> getAllTags(int size, int page, WebMvcLinkBuilder link) {
        try {
            int offset = pageService.getOffset(size, page);
            long totalSize = tagDao.getNumberOfTags();
            List<Tag> tags = tagDao.getAll(size, offset);
            List<Link> links = pageService.createLinks(size, page, totalSize, link);
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, totalSize);
            return PagedModel.of(tags, metadata, links);
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAGS_WERE_NOT_FOUND, e.getCause());
        }
    }

    @Override
    public Tag getMostPopularTag() {
        return tagDao.getMostPopularTag();
    }

    @Override
    public Tag getTag(long id) {
        try {
            if (tagDao.isTagExistById(id)) {
                return tagDao.getById(id);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_FOUND + id);
            }
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_FOUND + id, e.getCause());
        }
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        try {
            if (tagDao.isTagExistById(tag.getId())) {
                return tagDao.update(tag);
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_UPDATED + tag.getId());
            }
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, MSG_WITH_NAME_EXIST + tag.getName());

        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_UPDATED + tag.getId(), e.getCause());
        }
    }


    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        try {
            return tagDao.create(tag);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, MSG_TAG_EXIST + tag.getName(), e.getCause());

        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_CREATED + tag.getName(), e.getCause());
        }
    }

    @Override
    public Tag getTag(String name) {
        try {
            if (this.isTagExistByName(name)) {
                return tagDao.getByName(name);
            } else {
                throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_TAG_WAS_NOT_FOUND_BY_NAME + name);
            }
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_TAG_WAS_NOT_DELETED + id, e.getCause());
        }
    }

    @Override
    public boolean isTagExistByName(String name) {
        return tagDao.isTagExistByName(name);
    }
}
