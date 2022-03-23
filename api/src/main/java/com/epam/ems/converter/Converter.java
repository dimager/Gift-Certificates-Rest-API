package com.epam.ems.converter;

import com.epam.ems.dto.BaseDTO;
import com.epam.ems.entity.BaseEntity;

public interface Converter <T extends BaseEntity,K extends BaseDTO> {
    T convertToEntity(K k);
    K convertToDto(T t);
}
