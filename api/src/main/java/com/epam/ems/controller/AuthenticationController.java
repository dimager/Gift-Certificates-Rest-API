package com.epam.ems.controller;

import com.epam.ems.dto.AuthenticateUserDTO;
import com.epam.ems.dto.UserDTO;
import com.epam.ems.dto.converter.DtoConverter;
import com.epam.ems.entity.User;
import com.epam.ems.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthenticationController {
    private final UserService userService;
    private final DtoConverter dtoConverter;

    @Autowired
    public AuthenticationController(UserService userService, DtoConverter dtoConverter) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    /**
     * Allows creating user
     *
     * @param user user data
     * @return created user with id
     */
    @PostMapping("/sign-up")
    public UserDTO createUser(@RequestBody @Valid AuthenticateUserDTO user) {
        User createdUser = userService.create(dtoConverter.convertToEntity(user));
        UserDTO userDTO = dtoConverter.convertToDTO(createdUser);
        userDTO.add(linkTo(methodOn(OrderController.class)
                .getOrders(10, 1, Optional.of(createdUser.getId()), null)).withRel("Orders"));
        return userDTO;
    }

}
