package com.epam.ems.service;

import com.epam.ems.dao.OrderDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.OrderCertificate;
import com.epam.ems.entity.User;
import com.epam.ems.service.exception.ServiceException;
import com.epam.ems.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderDao orderDao;
    @Mock
    PageService pageService;
    @Mock
    UserService userService;
    @Mock
    CertificateService certificateService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderDao, userService, certificateService, pageService);
    }

    @Test
    void getOrder() {
        when(orderDao.isOrderExist(anyLong())).thenReturn(true).thenReturn(false);
        Order order = new Order();
        order.setId(1);
        when(orderDao.getOrder(anyLong())).thenReturn(order);

        assertAll(() -> assertEquals(orderService.getOrder(1), order),
                () -> assertThrows(ServiceException.class, () -> orderService.getOrder(anyLong())));

    }

    @Test
    void createOrder() {
        Order order = new Order();
        OrderCertificate orderCertificate = new OrderCertificate();
        orderCertificate.setAmount(4);
        Certificate certificate = new Certificate();
        certificate.setId(1);
        certificate.setDuration((short) 2);
        certificate.setPrice(new BigDecimal("10.00"));
        certificate.setDescription("description");
        orderCertificate.setCertificate(certificate);
        order.getOrderCertificates().add(orderCertificate);
        User user = new User();
        user.setUsername("user1");
        when(certificateService.getCertificate(anyLong())).thenReturn(certificate);
        when(orderDao.createOrder(order)).thenReturn(order);
        when(userService.getUser(anyLong())).thenReturn(user);

        BigDecimal cost = certificate.getPrice().multiply(BigDecimal.valueOf(orderCertificate.getAmount()));

        Order createdOrder = orderService.createOrder(1, order);
        assertAll(() -> assertEquals(cost, createdOrder.getCost()),
                () -> assertEquals(user, createdOrder.getUser()));

    }
}