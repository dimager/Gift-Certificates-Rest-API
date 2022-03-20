package com.epam.ems.controller;

import com.epam.ems.converter.AuthenticateUserConverter;
import com.epam.ems.converter.UserConverter;
import com.epam.ems.dto.AuthenticateUserDTO;
import com.epam.ems.dto.UserDTO;
import com.epam.ems.entity.User;
import com.epam.ems.exception.response.ExceptionWithCodeResponse;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AuthenticationController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final AuthenticateUserConverter authenticateUserConverter;

    @Autowired
    public AuthenticationController( UserService userService,
                                    UserConverter userConverter,
                                    AuthenticateUserConverter authenticateUserConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.authenticateUserConverter = authenticateUserConverter;
    }

    /**
     * Allows creating user
     *
     * @param user user data
     * @return created user with id
     */
    @PostMapping("/sign-up")
    public UserDTO createUser(@RequestBody @Valid AuthenticateUserDTO user) {
        User createdUser = userService.create(authenticateUserConverter.convertToDao(user));
        UserDTO userDTO = userConverter.convertToDto(createdUser);
        userDTO.add(linkTo(methodOn(OrderController.class)
                .getOrders(10, 1, Optional.of(createdUser.getId()), null)).withRel("Orders"));
        return userDTO;
    }



}
