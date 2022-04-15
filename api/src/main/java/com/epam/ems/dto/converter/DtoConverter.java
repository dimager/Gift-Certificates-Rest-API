package com.epam.ems.dto.converter;

import com.epam.ems.dto.AuthenticateUserDTO;
import com.epam.ems.dto.CertificateDTO;
import com.epam.ems.dto.OrderDTO;
import com.epam.ems.dto.TagDTO;
import com.epam.ems.dto.UserDTO;
import com.epam.ems.entity.Certificate;
import com.epam.ems.entity.Order;
import com.epam.ems.entity.Tag;
import com.epam.ems.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DtoConverter {

    private final ModelMapper modelMapper;

    public DtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToEntity(AuthenticateUserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public Certificate convertToEntity(CertificateDTO certificateDTO) {
        Certificate certificate = modelMapper.map(certificateDTO, Certificate.class);
        certificateDTO.getTags().forEach(tagDTO -> certificate.getTags().add(modelMapper.map(tagDTO, Tag.class)));
        return certificate;
    }

    public Order convertToEntity(OrderDTO orderDTO) {
        return modelMapper.map(orderDTO, Order.class);
    }

    public Tag convertToEntity(TagDTO tagDTO) {
        return modelMapper.map(tagDTO, Tag.class);
    }

    public UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
