package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.OrderCertificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping()
    public CollectionModel<Order> getOrders(@RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "page", defaultValue = "1") int page,
                                            @RequestParam(name = "userId", required = false) Optional<Long> userId) {
        CollectionModel<Order> orders = orderService.getAll(size, page, userId, linkTo(OrderController.class));
        orders.getContent().forEach(this::createLinks);
        return orders;
    }

    @GetMapping("{id}")
    public Order getOrder(@PathVariable long id) {
        Order order = orderService.getOrder(id);
        createLinks(order);
        return order;
    }

    private void createLinks(Order order) {
        order.add(linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel());
        order.add(linkTo(methodOn(UserController.class).getUser(order.getUser().getId())).withRel("User"));
        order.add(linkTo(methodOn(OrderController.class).getOrders(10, 1, Optional.of(order.getUser().getId()))).withRel("UserOrders"));
        for (OrderCertificate orderCertificate : order.getOrderCertificates()) {
            orderCertificate.add(linkTo(methodOn(OrderController.class).getOrder(order.getId())).withSelfRel());
            Certificate certificate = orderCertificate.getCertificate();
            if (!certificate.hasLink("self")) {
                certificate.add(linkTo(methodOn(CertificatesController.class).getCertificate(certificate.getId())).withSelfRel());
            }
            for (Tag tag : certificate.getTags()) {
                if (!tag.hasLink("self")) {
                    tag.add(linkTo(methodOn(TagController.class).getTag(tag.getId())).withSelfRel());
                }
                if (!tag.hasLink("Certificates")) {
                    tag.add(Link.of(linkTo(CertificatesController.class)
                                    .toUriComponentsBuilder().queryParam("tags", tag.getName()).build().toString())
                            .withRel("Certificates"));
                }
            }
        }
    }

    @PostMapping
    public Order createOrder(@RequestParam(required = false) Optional<Long> userId, @RequestBody @Valid Order order) {
        order = orderService.createOrder(userId.get(), order);
        this.createLinks(order);
        return order;
    }
}
