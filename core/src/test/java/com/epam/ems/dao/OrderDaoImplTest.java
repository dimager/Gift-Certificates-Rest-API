package com.epam.ems.dao;

import com.epam.ems.TestDaoConfig;
import com.epam.ems.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = {TestDaoConfig.class})
class OrderDaoImplTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    void getOrder() {
        assertAll(() -> assertDoesNotThrow(() -> orderDao.getOrder(200)),
                () -> assertThrows(EmptyResultDataAccessException.class, () -> orderDao.getOrder(-1)));
    }

    @Test
    void getAllOrders() {
        int pageSize = 12;
        assertTrue(orderDao.getAllOrders(pageSize, 0).size() == pageSize);
    }

    @Test
    void getUserOrders() {
        int pageSize = 12;
        long userId = 1;
        long missingUserId = -1;
        assertAll(() -> assertTrue(orderDao.getUserOrders(pageSize, 0, userId).size() > 0),
                () -> assertTrue(orderDao.getUserOrders(pageSize, 0, missingUserId).size() == 0));
    }

    @Test
    void isOrderExist() {
        long orderId = 201;
        long missingOrderId = -1;
        assertAll(() -> assertTrue(orderDao.isOrderExist(orderId)),
                () -> assertFalse(orderDao.isOrderExist(missingOrderId)));
    }

    @Test
    @Transactional
    void createOrder() {
        long orderId = 202;
        Order order = orderDao.getOrder(orderId);
        Order newOrder =  new Order();
        newOrder.setCost(new BigDecimal("1.0"));
        newOrder.setUser(order.getUser());
        newOrder.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
        newOrder.getOrderCertificates().addAll(order.getOrderCertificates());
        orderDao.createOrder(newOrder);

        assertAll(() -> assertEquals(order.getUser(),newOrder.getUser()),
                () -> assertTrue(orderDao.isOrderExist(newOrder.getId())));
        System.out.println("newOrder = " + newOrder);
    }
}