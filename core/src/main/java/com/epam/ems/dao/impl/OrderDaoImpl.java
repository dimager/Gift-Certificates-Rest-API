package com.epam.ems.dao.impl;

import com.epam.ems.dao.OrderDao;
import com.epam.ems.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final String FIND_BY_ID_EQUALS = "select o from Order o where o.id = :id";
    private static final String FIND_BY_USER_ID_EQUALS = "select o from Order o where o.user.id = :id";
    private static final String EXISTS_BY_ID_EQUALS = "select (count(o) > 0) from Order o where o.id = :id";
    private static final String FIND_ALL_ORDERS = "select o from Order o";
    private static final String COUNT_USER_ORDERS = "select count(o) from Order o where o.user.id = :id";
    private static final String COUNT_ALL_ORDERS = "select count(o) from Order o";
    private final EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order getOrder(long id) {
        TypedQuery<Order> typedQuery = entityManager.createQuery(FIND_BY_ID_EQUALS, Order.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<Order> getAllOrders(int size, int offset) {
        return entityManager.createQuery(FIND_ALL_ORDERS, Order.class)
                .setMaxResults(size)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<Order> getUserOrders(int size, int offset, long userId) {
        TypedQuery<Order> typedQuery = entityManager.createQuery(FIND_BY_USER_ID_EQUALS, Order.class);
        typedQuery.setParameter("id", userId);
        return typedQuery.setFirstResult(offset).setMaxResults(size).getResultList();
    }


    @Override
    public Long getNumberOfOrders() {
        return entityManager.createQuery(COUNT_ALL_ORDERS, Long.class).getSingleResult();
    }

    @Override
    public Long getNumberOfUserOrders(long userId) {
        TypedQuery<Long> typedQuery = entityManager.createQuery(COUNT_USER_ORDERS, Long.class);
        typedQuery.setParameter("id", userId);
        return typedQuery.getSingleResult();
    }

    @Override
    public boolean isOrderExist(long id) {
        TypedQuery<Boolean> query = entityManager.createQuery(EXISTS_BY_ID_EQUALS, Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Order createOrder(Order order) {
        entityManager.persist(order);
        return order;
    }
}
