package com.epam.ems.jwt.handler;

import com.epam.ems.entity.User;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import com.epam.ems.jwt.response.FailAuthenticationResponse;
import com.epam.ems.jwt.response.SuccessfulAuthenticationResponse;
import com.epam.ems.provider.MessageProvider;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
    private static final Logger LOGGER = LogManager.getLogger(AuthenticationHandler.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private String AUTH_FAIL = "auth.fail";
    private String AUTH_FAIL_CODE = "30111";
    private String AUTH_SUCCESS = "auth.success";
    private String AUTH_SUCCESS_CODE = "30200";

    @Autowired
    public AuthenticationHandler(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        final UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetail.getUsername());
        final String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole().name(), user.getId());
        SuccessfulAuthenticationResponse jwtResponse =
                new SuccessfulAuthenticationResponse(MessageProvider.getLocalizedMessage(AUTH_SUCCESS), user.getUsername(), AUTH_SUCCESS_CODE, token);
        String body = new ObjectMapper().writeValueAsString(jwtResponse);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        LOGGER.error(exception.getMessage());
        FailAuthenticationResponse jwtResponse =
                new FailAuthenticationResponse(HttpStatus.BAD_REQUEST, AUTH_FAIL_CODE, MessageProvider.getLocalizedMessage(AUTH_FAIL));
        String body = new ObjectMapper().writeValueAsString(jwtResponse);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(body);
        response.getWriter().flush();
    }

}
