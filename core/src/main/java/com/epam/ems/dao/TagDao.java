package com.epam.ems.dao;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;

import java.util.List;


public interface TagDao extends BaseDao<Tag> {
    Tag getByName(String name);
    Tag checkTag(Tag tag);
    void deleteTagRelations(long id);
    List<Certificate> getTagCertificates(String name);
}
