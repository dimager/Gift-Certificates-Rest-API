package com.epam.ems.dto;

import com.epam.ems.annotaion.ValidPassword;
import com.epam.ems.annotaion.ValidUsername;
import lombok.Data;

@Data
public class AuthenticateUserDTO extends BaseDTO{
    @ValidUsername
    private String username;
    @ValidPassword
    private String password;
}
