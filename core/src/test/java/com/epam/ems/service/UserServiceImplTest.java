package com.epam.ems.service;

import com.epam.ems.dao.UserDao;
import com.epam.ems.entity.User;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @Mock
    private PageService pageService;

    private List<User> userList = new ArrayList<>();
    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userDao, pageService);

    }


    @Test
    void getUser() {

        User user = new User();
        user.setUsername("newUsername");
        user.setId(1);
        when(userDao.isUserExist(anyLong())).thenReturn(true).thenReturn(false);
        when(userDao.getUser(anyLong())).thenReturn(user);
        assertAll(() -> assertEquals(user, userService.getUser(anyLong())),
                () -> assertThrows(ServiceException.class, () -> userService.getUser(anyLong())));
    }


    @Test
    void isUserExist() {
        when(userDao.isUserExist(anyLong())).thenReturn(false);
        assertAll(() -> assertThrows(ServiceException.class, () ->  userService.isUserExist(anyLong())));
    }

    @Test
    void create() {
        User user = new User();
        user.setUsername("newUsername");
        user.setId(1);

        when(userDao.create(user)).thenReturn(user).thenThrow(RuntimeException.class);
        assertAll(() -> assertEquals(user,userService.create(user)),
                () -> assertThrows(ServiceException.class, () -> userService.create(user)));
    }
}