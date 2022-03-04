package com.epam.ems.dao;

import com.epam.ems.entity.Order;

import java.util.List;

public interface OrderDao {
    /**
     * Allows getting order info
     *
     * @param id order if
     * @return order info
     */
    Order getOrder(long id);

    /**
     * Allows getting
     *
     * @param size
     * @param offset
     * @return list of all orders
     */
    List<Order> getAllOrders(int size, int offset);

    /**
     * Allows getting user orders
     *
     * @param size   page size
     * @param offset offset
     * @param userId user id
     * @return list of user`s orders
     */
    List<Order> getUserOrders(int size, int offset, long userId);

    /**
     * Allows getting total number of orders
     *
     * @return number of orders
     */
    Long getNumberOfOrders();

    /**
     * Allows getting number of user`s orders
     *
     * @param userId userId
     * @return number of user orders
     */
    Long getNumberOfUserOrders(long userId);

    /**
     * Allows checking order for existence
     *
     * @param id order id
     * @return true if exist - otherwise false
     */
    boolean isOrderExist(long id);


    /**
     * Allows creating order
     *
     * @param order order data
     * @return created order with id
     */
    Order createOrder(Order order);

}
