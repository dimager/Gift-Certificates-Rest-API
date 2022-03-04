package com.epam.ems.dao;

import com.epam.ems.entity.User;

import java.util.List;

public interface UserDao {
    /**
     * Allows getting user by id
     *
     * @param id user id
     * @return user data
     */
    User getUser(long id);

    /**
     * Allows getting list of all users
     *
     * @param size size of page
     * @param offset offset
     * @return list of users
     */
    List<User> getUsers(int size, int offset);

    /**
     * Allows checking user existence
     *
     * @param id user id
     * @return true if exist - otherwise false
     */
    boolean isUserExist(long id);

    /**
     * Allows creating user
     *
     * @param user user data
     * @return created user with id
     */
    User create(User user);

    /**
     * Allows getting total number of users
     *
     * @return number of users
     */
    long getNumberOfUsers();
}
