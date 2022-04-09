package com.epam.ems.service;

import com.epam.ems.dao.TagDao;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("dev")
class TagServiceTest {
    @Mock
    PageService pageService;
    @Mock
    private TagDao tagDao;
    private TagService tagService;
    private List<Tag> tags;
    private Tag tag;

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
        tagService = new TagServiceImpl(tagDao, pageService);
    }


    @Test
    void getTagById() {
        when(tagDao.isTagExistById(anyLong())).thenReturn(true).thenReturn(false);
        when(tagDao.getById(tag.getId())).thenReturn(tag);
        assertAll(
                () -> assertEquals(tag, tagService.getTag(tag.getId())),
                () -> assertThrows(ServiceException.class, () -> tagService.getTag(tag.getId())));
    }

    @Test
    void updateTag() {
        String newName = "newTagName";
        when(tagDao.isTagExistById(anyLong())).thenReturn(true);
        when(tagDao.update(tag))
                .thenAnswer((Answer<Tag>) invocation -> {
                    Tag tag = invocation.getArgument(0, Tag.class);
                    tag.setName(newName);
                    return tag;
                })
                .thenThrow(ServiceException.class);

        assertAll(
                () -> assertEquals(newName, tagService.updateTag(tag).getName()),
                () -> assertThrows(ServiceException.class, () -> tagService.updateTag(tag)));
    }

    @Test
    void createTag() {
        long generatedId = 100;
        Tag tag = new Tag();
        tag.setName("testTag");
        tag.setId(generatedId);

        when(tagDao.create(tag)).thenReturn(tag).thenThrow(DataIntegrityViolationException.class);

        assertAll(
                () -> assertEquals(tag, tagService.createTag(tag)),
                () -> assertThrows(ServiceException.class, () -> tagService.createTag(tag)));
    }
/*

    @Test
    void getTagByName() {
        String tagName = "tagname3";
        when(tagDao.isTagExistById(anyLong())).thenReturn(true);
        when(tagDao.getByName(tagName))
                .thenAnswer((Answer<Tag>) invocation -> {
                    String name = invocation.getArgument(0, String.class);
                    return tags.stream().filter(tag -> tag.getName().equals(name)).findFirst().get();
                });
        assertAll(
                () -> assertTrue(tags.contains(tagService.getTag(tagName))));
    }
*/


}
