package com.epam.ems.dao;

import com.epam.ems.entity.Tag;

public interface TagDao extends BaseDao<Tag> {

    /**
     * Allows getting number of all tags.
     *
     * @return number of all tags
     */
    Long getNumberOfTags();

    /**
     * Allows getting most popular tag
     *
     * @return most popular tag info
     */
    Tag getMostPopularTag();

    /**
     * Allows getting tag by name.
     *
     * @param name Tag name.
     * @return tag - if tag is found.
     */
    Tag getByName(String name);

    /**
     * Allows checking tag existence by tag id.
     *
     * @param id tag id
     * @return true - if tag is found, otherwise - false.
     */
    boolean isTagExistById(long id);

    /**
     * Allows checking tag existence by tag name.
     *
     * @param name tag name
     * @return true - if tag is found, otherwise - false.
     */
    boolean isTagExistByName(String name);
}
