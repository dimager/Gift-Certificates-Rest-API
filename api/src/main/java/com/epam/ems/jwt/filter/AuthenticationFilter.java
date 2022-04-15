package com.epam.ems.jwt.filter;

import com.epam.ems.dto.AuthenticateUserDTO;
import com.epam.ems.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);
    private static final String FAILED_ATTEMPT_AUTH_CODE = "40302";

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        super(authenticationManager);
        this.setFilterProcessesUrl("/login");
        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication auth;
        try {
            AuthenticateUserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), AuthenticateUserDTO.class);
            auth = this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        } catch (IOException e) {
            LOGGER.error(e);
            throw new JwtAuthenticationException(FAILED_ATTEMPT_AUTH_CODE, HttpStatus.FORBIDDEN);
        }
        return auth;
    }
}
