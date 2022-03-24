package com.epam.ems.jwt.response;

import lombok.Getter;

@Getter
public class SuccessfulAuthenticationResponse {
    private final String username;
    private final String code;
    private final String message;
    private final String token;

    public SuccessfulAuthenticationResponse(String message, String username, String code, String token) {
        this.username = username;
        this.message = message;
        this.code = code;
        this.token = token;
    }
}
