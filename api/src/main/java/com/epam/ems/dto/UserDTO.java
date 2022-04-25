package com.epam.ems.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDTO extends BaseDTO {
    private Long id;
    @NotNull
    private String username;

}
