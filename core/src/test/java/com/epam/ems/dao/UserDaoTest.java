package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = TestDaoConfig.class)
@EnableAutoConfiguration
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void getUser() {
        int userId = 1;
        int missingUserId = -1;
        assertAll(() -> assertDoesNotThrow(() -> userDao.getUser(userId)),
                () -> assertThrows(EmptyResultDataAccessException.class, () -> userDao.getUser(missingUserId)));
    }

    @Test
    void getUsers() {
        int pageLimit = 143;
        assertTrue(userDao.getUsers(pageLimit, 0).size() == pageLimit);
    }

    @Test
    void isUserExist() {
        int userId = 1;
        int missingUserId = -1;
        assertAll(() -> assertTrue(userDao.isUserExist(userId)),
                () -> assertFalse(userDao.isUserExist(missingUserId)));
    }

    @Test
    void create() {
        User user = new User();
        user.setUsername("newUser");
        final User[] createdUser = new User[1];
        assertAll(() -> createdUser[0] = userDao.create(user),
                () -> assertTrue(userDao.isUserExist(createdUser[0].getId())));

    }
}