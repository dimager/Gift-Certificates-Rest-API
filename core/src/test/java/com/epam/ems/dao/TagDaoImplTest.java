package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestDaoConfig.class, loader = AnnotationConfigContextLoader.class)
class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void delete() {
        Tag tag = tagDao.getById(10);
        Assertions.assertAll(
                () -> Assertions.assertTrue(tagDao.delete(tag.getId())),
                () -> Assertions.assertFalse(tagDao.getAll().contains(tag)),
                () -> Assertions.assertFalse(tagDao.delete(-1))
        );
    }

    @Test
    void getAll() {
        Assertions.assertFalse(tagDao.getAll().isEmpty());
    }

    @Test
    void getById() {
        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(Tag.class, tagDao.getById(13)),
                () -> Assertions.assertThrows(DataAccessException.class, () -> tagDao.getById(-1)));
    }

    @Test
    void create() {
        Tag tag = new Tag("testTag");
        Assertions.assertAll(
                () -> Assertions.assertInstanceOf(Tag.class, tagDao.create(tag)),
                () -> Assertions.assertEquals(tagDao.getById(tag.getId()), tag)
        );
    }

    @Test
    void getByName() {
        String tagName = "spa";
        String incorrectName = "spda";
        Assertions.assertAll(
                () -> Assertions.assertTrue(tagDao.getByName(tagName).getName().equals(tagName)),
                () -> Assertions.assertThrows(DataAccessException.class, () -> tagDao.getByName(incorrectName))
        );
    }

    @Test
    void checkTagForExistenceInDatabase() {
        Tag tagAuto = tagDao.getByName("auto");
        Tag tagTravel = tagDao.getByName("travel");
        Tag tagWithNewName = new Tag("tagWithNewName");
        Tag tagWithNewNameAndIncorrectId = new Tag(2134, "tagWithNewNameAndIncorrectId");
        Tag tagWithIncorrectIdAndCorrectName = new Tag(154, "travel");
        Tag tagWithCorrectIdAndNewName = new Tag(20, "newName");
        Tag tagWithCorrectName = new Tag("auto");

        Assertions.assertAll(
                () -> Assertions.assertTrue(tagDao.checkTagForExistenceInDatabase(tagWithNewName).getId() != 0),
                () -> Assertions.assertInstanceOf(Tag.class, tagDao.checkTagForExistenceInDatabase(tagWithCorrectIdAndNewName)),
                () -> Assertions.assertEquals(tagWithCorrectIdAndNewName, tagDao.getById(tagWithCorrectIdAndNewName.getId())),
                () -> Assertions.assertEquals(tagAuto, tagDao.checkTagForExistenceInDatabase(tagWithCorrectName)),
                () -> Assertions.assertTrue(tagDao.checkTagForExistenceInDatabase(tagWithNewNameAndIncorrectId).getId() != 2134),
                () -> Assertions.assertEquals(tagTravel.getId(), tagDao.checkTagForExistenceInDatabase(tagWithIncorrectIdAndCorrectName).getId())
        );

    }
}