package com.epam.ems.service.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.entity.User;
import com.epam.ems.service.PageService;
import com.epam.ems.service.UserService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private static final String MSG_USER_WAS_NOT_FOUND = "30502;User was not found. User id=";
    private static final String MSG_USER_WAS_NOT_CREATED = "30502;User was not created. User name=";
    private static final String USER_EXIST = ". Username already exists";
    private final UserDao userDao;
    private final PageService pageService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserDao userDao, PageService pageService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.pageService = pageService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public User getUser(long id) {
        if (userDao.isUserExist(id)) {
            return userDao.getUser(id);
        } else {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_FOUND + id);
        }
    }

    @Override
    public User getUser(String username) {
        return userDao.getUser(username);
    }

    @Override
    public PagedModel<User> getUsers(int size, int page, WebMvcLinkBuilder link) {
        List<User> userList = userDao.getUsers(size, page);
        long totalSize = userDao.getNumberOfUsers();
        pageService.isPageExist(page, totalSize, pageService.getOffset(size, page));
        List<Link> links = pageService.createLinks(size, page, totalSize, link);
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, totalSize);
        return PagedModel.of(userList, metadata, links);
    }


    @Override
    public boolean isUserExist(long id) {
        if (userDao.isUserExist(id)) {
            return true;
        } else {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_FOUND + id);
        }
    }

    @Override
    @Transactional
    public User create(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDao.create(user);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, MSG_USER_WAS_NOT_CREATED + user.getUsername() + USER_EXIST);
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_USER_WAS_NOT_CREATED + user.getUsername());
        }
    }
}
