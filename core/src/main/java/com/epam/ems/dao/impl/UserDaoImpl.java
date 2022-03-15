package com.epam.ems.dao.impl;

import com.epam.ems.dao.UserDao;
import com.epam.ems.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private final static String SELECT_USER_BY_ID = "SELECT u FROM User u where u.id = :id";
    private final static String SELECT_ALL_USERS = "SELECT u FROM User u";
    private final static String EXISTS_BY_ID_EQUALS = "select (count(u) > 0) from User u where u.id = :id";
    private final static String SELECT_USER_BY_USERNAME = "SELECT u FROM User u where u.username = :username";


    private final EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getUser(long id) {
        TypedQuery<User> typedQuery = entityManager.createQuery(SELECT_USER_BY_ID, User.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public User getUser(String username) {
        TypedQuery<User> typedQuery =  entityManager.createQuery(SELECT_USER_BY_USERNAME,User.class);
        typedQuery.setParameter("username", username);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<User> getUsers(int size, int offset) {
        return entityManager.createQuery(SELECT_ALL_USERS, User.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public boolean isUserExist(long id) {
        TypedQuery<Boolean> typedQuery = entityManager.createQuery(EXISTS_BY_ID_EQUALS, Boolean.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Override
    public long getNumberOfUsers() {
        return entityManager.createQuery(SELECT_ALL_USERS, User.class)
                .getResultList().size();
    }
}
