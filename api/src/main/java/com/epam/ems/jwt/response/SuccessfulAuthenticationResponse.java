package com.epam.ems.jwt.response;

import lombok.Data;

@Data
public class SuccessfulAuthenticationResponse {
    private final String username;
    private int code = 30200;
    private final String message;
    private final String token;

    public SuccessfulAuthenticationResponse(String message, String username, String token) {
        this.username = username;
        this.message = message;
        this.token = token;
    }
}
