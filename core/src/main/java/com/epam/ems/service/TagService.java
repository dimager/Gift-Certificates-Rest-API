package com.epam.ems.service;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    @Autowired
    void setCertificateService(CertificateService certificateService);

    List<Tag> getAllTags();

    Tag getTag(long id);

    Tag updateTag(Tag tag);

    Tag createTag(Tag tag);

    Tag getTag(String name);

    boolean deleteTag(long id);

    Tag checkTag(Tag tag);

    List<Certificate> getTagCertificates(String name);
}
