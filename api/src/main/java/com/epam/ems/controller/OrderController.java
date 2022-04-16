package com.epam.ems.controller;

import com.epam.ems.dto.OrderDTO;
import com.epam.ems.dto.converter.DtoConverter;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.OrderCertificate;
import com.epam.ems.entity.Tag;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.service.OrderService;
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

import static com.epam.ems.security.Permission.ORDER_READ;
import static com.epam.ems.security.Permission.USER_ORDER_READ;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private static final String ACCESS_DENIED_CODE = "40300";
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final DtoConverter dtoConverter;

    @Autowired
    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider, DtoConverter dtoConverter) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.dtoConverter = dtoConverter;
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
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getUserPermission()))) {
            CollectionModel<Order> orders = orderService.getAll(size, page, Optional.of(jwtTokenProvider.getId(token)), linkTo(OrderController.class));
            orders.getContent().forEach(this::createLinks);
            return orders;
        } else if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ORDER_READ.getUserPermission()))) {
            CollectionModel<Order> orders = orderService.getAll(size, page, userId, linkTo(OrderController.class));
            orders.getContent().forEach(this::createLinks);
            return orders;
        } else {
            throw new JwtAuthenticationException(ACCESS_DENIED_CODE, HttpStatus.FORBIDDEN);
        }
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
        this.createLinks(order);
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getUserPermission()))) {
            if (order.getUser().getId() == jwtTokenProvider.getId(token)) {
                return order;
            } else {
                throw new JwtAuthenticationException(ACCESS_DENIED_CODE, HttpStatus.FORBIDDEN);
            }
        }
        return order;
    }

    /**
     * Allows creating order for user
     *
     * @param orderDTO order data
     * @return created certificate with id
     */
    @PostMapping
    @PreAuthorize("hasAuthority('order:write') or hasAuthority('userorder:write')")
    public Order createOrder(@RequestBody @Valid OrderDTO orderDTO,
                             @RequestHeader(name = "Authorization") String token) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USER_ORDER_READ.getUserPermission()))) {
            Order order = orderService.createOrder(jwtTokenProvider.getId(token), dtoConverter.convertToEntity(orderDTO));
            this.createLinks(order);
            return order;
        }
        throw new JwtAuthenticationException(ACCESS_DENIED_CODE, HttpStatus.FORBIDDEN);
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
                certificate.add(linkTo(methodOn(CertificatesController.class).getImage(certificate.getId())).withRel("image"));
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
