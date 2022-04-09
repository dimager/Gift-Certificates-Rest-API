package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TestDaoConfig.class})
@ActiveProfiles("dev")
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
    @Transactional
    void create() {
        User user = new User();
        user.setUsername("newUser321");
        user.setPassword("password");
        User createdUser = userDao.create(user);
        assertAll(() -> assertTrue(userDao.isUserExist(createdUser.getId())));

    }
}