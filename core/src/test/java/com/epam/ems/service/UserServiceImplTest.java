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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder passwordEncoder;


    private UserService userService;
    @Mock
    private PageService pageService;

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userDao, pageService, passwordEncoder);
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

   /* @Test
    void create() {
        User user = new User();
        user.setUsername("newUsername13");
        user.setPassword("password");

        when(userDao.create(any(User.class))).thenReturn(user).thenThrow(RuntimeException.class);
        when(passwordEncoder.encode(any())).thenReturn("password");
        assertAll(() -> assertEquals(user,userService.create(any(User.class))),
                () -> assertThrows(ServiceException.class, () -> userService.create(any(User.class))));
    }*/
}