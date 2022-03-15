package com.epam.ems.dto;

import lombok.Data;

@Data
public class UserDTO extends BaseDTO {
    private Long id;
    private String username;
}
