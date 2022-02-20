package com.epam.ems.service;

import com.epam.ems.entity.Order;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Optional;

public interface OrderService {
    CollectionModel<Order> getAll(int size, int page, Optional<Long> userId, WebMvcLinkBuilder link);

    Order getOrder(long id);

    Order createOrder(long id, Order order);
}
