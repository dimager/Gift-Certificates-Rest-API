package com.epam.ems.service.impl;

import com.epam.ems.dao.OrderDao;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.OrderCertificate;
import com.epam.ems.service.CertificateService;
import com.epam.ems.service.OrderService;
import com.epam.ems.service.PageService;
import com.epam.ems.service.UserService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String MSG_ORDER_WAS_NOT_FOUND = "30602;Order was not found. Order id=";
    private static final String MSG_ORDER_WAS_NOT_CREATED = "30605;Order was not created. Order id=";
    private static final String MSG_ORDER_WERE_NOT_FOUND = "30603;Orders were not found. Wrong user";

    private final OrderDao orderDao;
    private final UserService userService;
    private final CertificateService certificateService;
    private final PageService pageService;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserService userService, CertificateService certificateService, PageService pageService) {
        this.orderDao = orderDao;
        this.userService = userService;
        this.certificateService = certificateService;
        this.pageService = pageService;
    }

    @Override
    @Transactional
    public CollectionModel<Order> getAll(int size, int page, Optional<Long> userId, WebMvcLinkBuilder link) {
        try {

            long totalSize;
            int offset = pageService.getOffset(size, page);
            HashMap<String, Number> extraParams = new HashMap<>();
            List<Order> orders;
            if (userId.isPresent() && userService.isUserExist(userId.get())) {
                totalSize = orderDao.getNumberOfUserOrders(userId.get());
                pageService.isPageExist(page, totalSize, offset);
                orders = orderDao.getUserOrders(size, offset, userId.get());
                extraParams.put("userId", userId.get());
            } else {
                totalSize = orderDao.getNumberOfOrders();
                pageService.isPageExist(page, totalSize, offset);
                orders = orderDao.getAllOrders(size, offset);
            }
            for (Order order : orders) {
                order.getOrderCertificates().forEach(oc -> oc.getCertificate().setPrice(oc.getPrice()));
            }
            PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(size, page, totalSize);
            List<Link> links = pageService.createLinksWithNumberParameters(size, page, totalSize, link, extraParams);
            return PagedModel.of(orders, metadata, links);
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_ORDER_WERE_NOT_FOUND, e);
        }
    }

    @Override
    public Order getOrder(long id) {
        try {
            if (orderDao.isOrderExist(id)) {
                Order order = orderDao.getOrder(id);
                order.getOrderCertificates().forEach(oc -> oc.getCertificate().setPrice(oc.getPrice()));
                return order;
            } else {
                throw new ServiceException(HttpStatus.NOT_FOUND, MSG_ORDER_WAS_NOT_FOUND + id);
            }
        } catch (RuntimeException e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_ORDER_WAS_NOT_FOUND + id, e);
        }
    }

    @Override
    @Transactional
    public Order createOrder(long id, Order order) {
        try {
            order.setUser(userService.getUser(id));
            order.setPurchaseDate(Timestamp.valueOf(LocalDateTime.now()));
            List<OrderCertificate> certificatesIdInOrder = new ArrayList<>(order.getOrderCertificates());
            order.getOrderCertificates().clear();
            order.setCost(BigDecimal.ZERO);
            for (OrderCertificate orderCertificate : certificatesIdInOrder) {
                Certificate certificate = certificateService.getCertificate(orderCertificate.getCertificate().getId());
                OrderCertificate oc = new OrderCertificate();
                oc.setOrder(order);
                oc.setCertificate(certificate);
                oc.setAmount(orderCertificate.getAmount());
                oc.setPrice(oc.getCertificate().getPrice());
                order.setCost(order.getCost().add(oc.getPrice().multiply(BigDecimal.valueOf(oc.getAmount()))));
                order.getOrderCertificates().add(oc);
            }
            return orderDao.createOrder(order);
        } catch (ServiceException e) {
            throw new ServiceException(e.getStatus(), e.getMessage(), e.getCause());
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new ServiceException(HttpStatus.NOT_FOUND, MSG_ORDER_WAS_NOT_CREATED + order.getId(), e.getCause());
        }

    }
}
