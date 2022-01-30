package com.epam.ems.service;

import com.epam.ems.dao.CertificateDao;
import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.CertificateServiceImpl;
import com.epam.ems.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private CertificateDao certificateDao;
    @Mock
    private CertificateService certificateService;


    private TagService tagService;
    private List<Tag> tags;
    private Tag tag;
    private DataAccessException dataAccessException = new DataAccessException("message") {
        @Override
        public String getMessage() {
            return super.getMessage();
        }
    };

    @BeforeEach
    void setUp() {
        tags = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setName("tagname" + i);
            tags.add(tag);
        }
        tag = tags.get(0);
        tagService = new TagServiceImpl(tagDao);
        certificateService = new CertificateServiceImpl(certificateDao);
        tagService.setCertificateService(certificateService);
    }


    @Test
    void getAllTags() {
        when(tagDao.getAll()).thenReturn(tags).thenReturn(tags).thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertEquals(tags, tagService.getAllTags()),
                () -> Assertions.assertTrue(tagService.getAllTags().contains(tag)),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.getAllTags()));
    }

    @Test
    void getTagById() {
        when(tagDao.getById(tag.getId())).thenReturn(tag).thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertEquals(tag, tagDao.getById(tag.getId())),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.getTag(tag.getId())));
    }

    @Test
    void updateTag() {
        String newName = "newTagName";
        when(tagDao.update(tag))
                .thenAnswer((Answer<Tag>) invocation -> {
                    Tag tag = invocation.getArgument(0, Tag.class);
                    tag.setName(newName);
                    return tag;
                })
                .thenThrow(dataAccessException);

        Assertions.assertAll(
                () -> Assertions.assertEquals(newName, tagService.updateTag(tag).getName()),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.updateTag(tag)));
    }

    @Test
    void createTag() {
        long generatedId = 100;
        Tag tag = new Tag();
        tag.setName("testTag");
        when(tagDao.create(tag))
                .thenAnswer((Answer<Tag>) invocation -> {
                    Tag tmpTag = invocation.getArgument(0, Tag.class);
                    tmpTag.setId(generatedId);
                    return tmpTag;
                })
                .thenThrow(dataAccessException);

        Assertions.assertAll(() -> tag.setName("newName"),
                () -> Assertions.assertEquals(generatedId, tagService.createTag(tag).getId()),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.createTag(tag)));
    }

    @Test
    void getTagByName() {
        String tagName = "tagname3";
        when(tagDao.getByName(tagName))
                .thenAnswer((Answer<Tag>) invocation -> {
                    String name = invocation.getArgument(0, String.class);
                    return tags.stream().filter(tag -> tag.getName().equals(name)).findFirst().get();
                })
                .thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertTrue(tags.contains(tagService.getTag(tagName))),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.getTag(tagName)));
    }

    @Test
    void deleteTag() {
        when(tagDao.delete(anyLong())).thenReturn(true).thenReturn(false).thenThrow(dataAccessException);
        Assertions.assertAll(
                () -> Assertions.assertTrue(tagService.deleteTag(anyLong())),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.deleteTag(anyLong())),
                () -> Assertions.assertThrows(ServiceException.class, () -> tagService.deleteTag(anyLong())));
    }

    @Test
    void getTagCertificates() {
        tagDao.getTagCertificates("spa").stream().forEach(System.out::println);

    }
}