package com.epam.ems.service.impl;

import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ComponentScan("com.epam.ems")
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAllTags() {
        return tagDao.getAll();
    }

    @Override
    public Tag getTag(long id) {
        return tagDao.getById(id);
    }

    @Override
    public Tag updateTag(Tag tag) {
        return tagDao.update(tag);
    }

    @Override
    public Tag createTag(Tag tag) {
        return tagDao.create(tag);
    }

}
