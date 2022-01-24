package com.epam.ems.service;

import com.epam.ems.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAllTags();

    Tag getTag(long id);

    Tag updateTag(Tag tag);

    Tag createTag(Tag tag);
}
