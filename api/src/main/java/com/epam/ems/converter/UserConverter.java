package com.epam.ems.converter;

import com.epam.ems.dto.UserDTO;
import com.epam.ems.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDTO> {
    @Override
    public User convertToDao(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        return user;
    }

    @Override
    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        return userDTO;
    }
}
