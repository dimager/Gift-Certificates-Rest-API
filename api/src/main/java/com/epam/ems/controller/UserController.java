package com.epam.ems.controller;

import com.epam.ems.entity.User;
import com.epam.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping
    public List<User> getUsers(@RequestParam(defaultValue = "10") int limit,
                               @RequestParam(defaultValue = "0") int offset) {
        return userService.getUsers(limit, offset);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        User user = userService.getUser(id);
        user.getOrders().forEach(order -> order.add(linkTo(methodOn(OrderController.class).getOrder(order.getId())).withRel("orderInfo")));
        return user;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

}
