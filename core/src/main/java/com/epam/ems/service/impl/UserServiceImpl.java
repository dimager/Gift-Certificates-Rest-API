package com.epam.ems.service.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.entity.User;
import com.epam.ems.service.UserService;
import com.epam.ems.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private static final String MSG_USER_WAS_NOT_FOUND = "30502;User was not found. User id=";
    private static final String MSG_USER_WAS_NOT_CREATED = "30502;User was not created. User name=";
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User getUser(long id) {
        if (userDao.isUserExist(id)) {
            return userDao.getUser(id);
        } else {
            logger.debug(MSG_USER_WAS_NOT_FOUND + id);
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_FOUND + id);
        }
    }

    @Override
    public List<User> getUsers(int limit, int offset) {
        return userDao.getUsers(limit, offset);
    }


    @Override
    public boolean isUserExist(long id) {
        if (userDao.isUserExist(id)) {
            return true;
        } else {
            logger.debug(MSG_USER_WAS_NOT_FOUND + id);
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_FOUND + id);
        }
    }

    @Override
    @Transactional
    public User create(User user) {
        try {
            return userDao.create(user);
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_CREATED + user.getUsername());
        }
    }
}
