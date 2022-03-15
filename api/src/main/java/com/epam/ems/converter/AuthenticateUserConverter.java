package com.epam.ems.converter;

import com.epam.ems.dto.AuthenticateUserDTO;
import com.epam.ems.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateUserConverter implements Converter<User, AuthenticateUserDTO> {
    @Override
    public User convertToDao(AuthenticateUserDTO authenticateUserDTO) {
        User user = new User();
        user.setUsername(authenticateUserDTO.getUsername());
        user.setPassword(authenticateUserDTO.getPassword());
        return user;
    }

    @Override
    public AuthenticateUserDTO convertToDto(User user) {
        AuthenticateUserDTO userDTO = new AuthenticateUserDTO();
        userDTO.setUsername(userDTO.getUsername());
        return userDTO;
    }
}
