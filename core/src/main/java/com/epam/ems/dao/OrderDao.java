package com.epam.ems.dao;

import com.epam.ems.entity.Order;

import java.util.List;

public interface OrderDao {
    Order getOrder(long id);

    List<Order> getAllOrders(int size, int page);

    List<Order> getUserOrders(int limit, int offset, long userId);

    Long getNumberOfOrders();

    Long getNumberOfUserOrders(long userId);

    boolean isOrderExist(long id);

    Order createOrder(Order order);

}
