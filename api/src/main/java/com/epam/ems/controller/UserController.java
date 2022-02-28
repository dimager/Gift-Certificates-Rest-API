package com.epam.ems.controller;

import com.epam.ems.entity.User;
import com.epam.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Allows getting list of users
     *
     * @param size number of users per page
     * @param page number of page
     * @return list of users
     */
    @GetMapping
    public CollectionModel<User> getUsers(@RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "1") int page) {
        PagedModel<User> userList = userService.getUsers(size, page, linkTo(UserController.class));
        userList.forEach(user -> user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()));
        userList.forEach(user -> user.getOrders().
                forEach(order -> order.add(linkTo(methodOn(OrderController.class).getOrder(order.getId())).withRel("Order"))));
        return userList;
    }

    /**
     * Allows getting user info
     *
     * @param id user id
     * @return user data
     */
    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        user.getOrders().forEach(order -> order.add(linkTo(methodOn(OrderController.class).getOrder(order.getId())).withRel("Order")));
        return user;
    }

    /**
     * Allows creating user
     *
     * @param user user data
     * @return created user with id
     */
    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

}
