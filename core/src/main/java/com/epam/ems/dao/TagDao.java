package com.epam.ems.dao;

import com.epam.ems.entity.Tag;

public interface TagDao extends BaseDao<Tag> {
    Tag getByName(String name);
    Tag checkTagForExistenceInDatabase(Tag tag);
    boolean isTagExistByName(String name);
}
