package com.epam.ems.service;

import com.epam.ems.entity.User;

import java.util.List;

public interface UserService {
    User getUser(long id);

    List<User> getUsers(int limit, int offset);

    boolean isUserExist(long id);

    User create(User user);
}
