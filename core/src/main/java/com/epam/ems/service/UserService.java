package com.epam.ems.service;

import com.epam.ems.entity.User;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public interface UserService {
    /**
     * Allows getting user by id
     *
     * @param id user id
     * @return uset info
     */
    User getUser(long id);


    /**
     * Allows getting user by username
     *
     * @param username user id
     * @return uset info
     */
    User getUser(String  username);

    /**
     * Allows getting list of users
     *
     * @param size page size
     * @param page number of page
     * @param link link to controller
     * @return list of users
     */
    PagedModel<User> getUsers(int size, int page, WebMvcLinkBuilder link);

    /**
     * Allows checking user for existence in DB
     *
     * @param id user id
     * @return true if user exists
     */
    boolean isUserExist(long id);

    /**
     * Allows creating user
     *
     * @param user user data
     * @return created user with id
     */
    User create(User user);
}
