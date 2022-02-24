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
    private EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order getOrder(long id) {
        TypedQuery<Order> typedQuery = entityManager.createNamedQuery("Order.findByIdEquals", Order.class);
        typedQuery.setParameter("id", id);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<Order> getAllOrders(int size, int offset) {
        return entityManager.createNamedQuery("Order.findAll", Order.class)
                .setMaxResults(size)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<Order> getUserOrders(int size, int offset, long userId) {
        TypedQuery<Order> typedQuery = entityManager.createNamedQuery("Order.findByUser_IdEquals", Order.class);
        typedQuery.setParameter("id", userId);
        return typedQuery.setFirstResult(offset).setMaxResults(size).getResultList();
    }


    @Override
    public Long getNumberOfOrders() {
        return entityManager.createNamedQuery("Order.countBy", Long.class).getSingleResult();
    }

    @Override
    public Long getNumberOfUserOrders(long userId) {
        TypedQuery<Long> typedQuery = entityManager.createNamedQuery("Order.countByUser_IdEquals", Long.class);
        typedQuery.setParameter("id", userId);
        return typedQuery.getSingleResult();
    }

    @Override
    public boolean isOrderExist(long id) {
        TypedQuery<Boolean> query = entityManager.createNamedQuery("Order.existsByIdEquals", Boolean.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public Order createOrder(Order order) {
        entityManager.persist(order);
        return order;
    }
}
