package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = {TestDaoConfig.class})
@ActiveProfiles("dev")
class TagDaoImplTest {
    @Autowired
    private TagDao tagDao;

    @Test
    @Transactional
    void delete() {
        long tagId = 100;
        assertAll(() -> assertDoesNotThrow(() -> tagDao.getById(tagId)),
                () -> assertTrue(tagDao.delete(tagId)),
                () -> assertThrows(EmptyResultDataAccessException.class, () -> tagDao.getById(tagId)));
    }

    @Test
    void getAll() {
        int pageSize = 13;
        assertAll(() -> assertEquals(pageSize, tagDao.getAll(pageSize, 0).size()));
    }

    @Test
    void getById() {
        assertAll(() -> assertDoesNotThrow(() -> tagDao.getById(1)),
                () -> assertThrows(EmptyResultDataAccessException.class, () -> tagDao.getById(10000)));
    }

    @Test
    @Transactional
    void create() {
        Tag tag = new Tag("testTag");
        assertAll(
                () -> assertInstanceOf(Tag.class, tagDao.create(tag)),
                () -> assertEquals(tagDao.getById(tag.getId()), tag)
        );
    }

    @Test
    @Transactional
    void update() {
        Tag tag = tagDao.getById(200);
        tag.setName("newName");
        assertAll(() -> assertDoesNotThrow(() -> tagDao.update(tag)),
                () -> assertEquals(tag.getName(), tagDao.getById(200).getName()));
    }

    @Test
    void getByName() {
        String tagName = "video1";
        assertAll(
                () -> assertEquals(tagDao.getByName(tagName).getName(), tagName)
        );
    }

    @Test
    void isTagExistByName() {
        String name = "video1";
        String missingTagName = "missingTag";

        assertAll(() -> assertTrue(tagDao.isTagExistByName(name)),
                () -> assertFalse(tagDao.isTagExistByName(missingTagName)));
    }
}