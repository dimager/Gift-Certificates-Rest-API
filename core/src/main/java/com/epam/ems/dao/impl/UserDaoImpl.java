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
    private EntityManager entityManager;

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
    public List<User> getUsers(int limit, int offset) {
        return entityManager.createQuery(SELECT_ALL_USERS, User.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public boolean isUserExist(long id) {
        TypedQuery<Boolean> typedQuery = entityManager.createNamedQuery("User.existsByIdEquals", Boolean.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
