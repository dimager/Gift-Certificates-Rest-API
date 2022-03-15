package com.epam.ems.jwt.handler;

import com.epam.ems.entity.User;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.jwt.response.FailAuthenticationResponse;
import com.epam.ems.jwt.response.SuccessfulAuthenticationResponse;
import com.epam.ems.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
    private static final Logger logger = LogManager.getLogger(AuthenticationHandler.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthenticationHandler(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        final UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetail.getUsername());
        final String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name(), user.getId());
        SuccessfulAuthenticationResponse jwtResponse =
                new SuccessfulAuthenticationResponse("Successfully logged in", user.getUsername(), token);
        String body = new ObjectMapper().writeValueAsString(jwtResponse);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        logger.error(exception.getMessage());
        FailAuthenticationResponse jwtResponse =
                new FailAuthenticationResponse(HttpStatus.UNAUTHORIZED,30111,"Incorrect username or password");
        String body = new ObjectMapper().writeValueAsString(jwtResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
        response.getWriter().flush();
    }

}
