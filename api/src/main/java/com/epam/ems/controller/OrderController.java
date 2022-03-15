package com.epam.ems.controller;

import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.OrderCertificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.exception.ControllerException;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.service.OrderService;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static com.epam.ems.security.Permission.USER_ORDER_READ;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Allows getting list of all orders or user orders
     *
     * @param size   - number of order per page
     * @param page   - number of page
     * @param userId - user id
     * @return list of certificates
     */
    @GetMapping()
    @PreAuthorize("hasAuthority('order:read') or hasAuthority('userorder:read')")
    public CollectionModel<Order> getOrders(
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "userId", required = false) Optional<Long> userId,
            @RequestHeader(name = "Authorization") String token) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getPermission()))) {
            if (userId.isPresent() && userId.get() == jwtTokenProvider.getId(token)) {
                CollectionModel<Order> orders = orderService.getAll(size, page, userId, linkTo(OrderController.class));
                orders.getContent().forEach(this::createLinks);
                return orders;
            } else {
                throw new JwtAuthenticationException("Access denied", HttpStatus.FORBIDDEN);
            }
        }
        CollectionModel<Order> orders = orderService.getAll(size, page, userId, linkTo(OrderController.class));
        orders.getContent().forEach(this::createLinks);
        return orders;
    }

    /**
     * Allows getting order
     *
     * @param id order id
     * @return order
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('order:read','userorder:read')")
    public Order getOrder(@RequestHeader(name = "Authorization") String token,
                          @PathVariable long id) {
        Order order = orderService.getOrder(id);
        createLinks(order);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getPermission()))) {
            if (order.getUser().getId() == jwtTokenProvider.getId(token)) {
                return order;
            } else {
                throw new JwtAuthenticationException("Access denied", HttpStatus.FORBIDDEN);
            }
        }
        return order;
    }

    /**
     * Allows creating order for user
     *
     * @param userId user id
     * @param order  order data
     * @return created certificate with id
     */
    @PostMapping
    @PreAuthorize("hasAuthority('order:write') or hasAuthority('userorder:write')")
    public Order createOrder(@RequestParam(required = false) Optional<Long> userId,
                             @RequestBody @Valid Order order,
                             @RequestHeader(name = "Authorization") String token) {
        if (userId.isPresent()) {
            if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getPermission()))) {
                if (userId.get() == jwtTokenProvider.getId(token)) {
                    order = orderService.createOrder(userId.get(), order);
                    this.createLinks(order);
                    return order;
                } else {
                    throw new JwtAuthenticationException("Access denied", HttpStatus.FORBIDDEN);
                }
            } else {
                order = orderService.createOrder(userId.get(), order);
                this.createLinks(order);
                return order;
            }
        } else {
            throw new ControllerException(HttpStatus.BAD_REQUEST, "30605;userId is absent");
        }
    }

    private void createLinks(Order order) {
        order.add(linkTo(methodOn(OrderController.class).getOrder(null, order.getId())).withSelfRel());
        order.add(linkTo(methodOn(UserController.class).getUser(null, order.getUser().getId())).withRel("User"));
        order.add(linkTo(methodOn(OrderController.class).getOrders(10, 1, Optional.of(order.getUser().getId()), null)).withRel("UserOrders"));
        for (OrderCertificate orderCertificate : order.getOrderCertificates()) {
            orderCertificate.add(linkTo(methodOn(OrderController.class).getOrder(null, order.getId())).withSelfRel());
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
}
