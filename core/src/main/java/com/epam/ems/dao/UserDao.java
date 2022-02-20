package com.epam.ems.dao;

import com.epam.ems.entity.User;

import java.util.List;

public interface UserDao {
    User getUser(long id);

    List<User> getUsers(int limit, int offset);

    boolean isUserExist(long id);

    User create(User user);
}
