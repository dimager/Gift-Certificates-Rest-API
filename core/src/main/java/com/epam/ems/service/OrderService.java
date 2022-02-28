package com.epam.ems.service;

import com.epam.ems.entity.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Optional;

public interface OrderService {
    /**
     * Allows getting list of all orders or user orders
     *
     * @param size   - number of order per page
     * @param page   - number of page
     * @param userId - user id
     * @param link link to controller
     * @return list of certificates
     */
    CollectionModel<Order> getAll(int size, int page, Optional<Long> userId, WebMvcLinkBuilder link);

    /**
     * Allows getting order
     *
     * @param id order id
     * @return order
     */
    Order getOrder(long id);

    /**
     * Allows creating order for user
     *
     * @param id    user id
     * @param order order data
     * @return created certificate with id
     */
    Order createOrder(long id, Order order);
}
