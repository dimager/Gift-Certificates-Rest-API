package com.epam.ems.controller;

import com.epam.ems.dto.UserDTO;
import com.epam.ems.dto.converter.DtoConverter;
import com.epam.ems.entity.User;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.ems.security.Permission.USERINFO_READ;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    private static final String ACCESS_DENIED_CODE = "40300";
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private final DtoConverter dtoConverter;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, ModelMapper modelMapper, DtoConverter dtoConverter) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.dtoConverter = dtoConverter;
    }


    /**
     * Allows getting list of users
     *
     * @param size number of users per page
     * @param page number of page
     * @return list of users
     */
    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public CollectionModel<UserDTO> getUsers(@RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "1") int page) {
        PagedModel<User> userPageModel = userService.getUsers(size, page, linkTo(UserController.class));
        List<UserDTO> userDTOList = userPageModel.getContent().stream().map(user -> dtoConverter.convertToDTO(user)).collect(Collectors.toList());
        PagedModel<UserDTO> userDTOPagedModel = PagedModel.of(userDTOList, userPageModel.getMetadata(), userPageModel.getLinks());
        for (UserDTO user : userDTOPagedModel.getContent()) {
            user.add(linkTo(methodOn(OrderController.class).getOrders(10, 1, Optional.of(user.getId()), null)).withRel("Orders"));
        }
        return userDTOPagedModel;
    }

    /**
     * Allows getting user info
     *
     * @param id user id
     * @return user data
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('user:read') or hasAuthority('userinfo:read')")
    public UserDTO getUser(@RequestHeader(name = "Authorization") String token,
                           @PathVariable long id) {
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(USERINFO_READ.getPermission()))) {
            if (id == jwtTokenProvider.getId(token)) {
                UserDTO user = dtoConverter.convertToDTO(userService.getUser(id));
                user.add(linkTo(methodOn(OrderController.class).getOrders(10, 1, Optional.of(id), null)).withRel("Orders"));
            } else {
                throw new JwtAuthenticationException(ACCESS_DENIED_CODE, HttpStatus.FORBIDDEN);
            }
        }
        UserDTO user = dtoConverter.convertToDTO(userService.getUser(id));
        user.add(linkTo(methodOn(OrderController.class).getOrders(10, 1, Optional.of(id), null)).withRel("Orders"));
        return user;
    }


}
