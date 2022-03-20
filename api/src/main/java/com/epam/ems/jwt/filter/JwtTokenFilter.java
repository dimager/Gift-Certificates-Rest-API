package com.epam.ems.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.epam.ems.provider.MessageProvider;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.exception.response.ExceptionWithCodeResponse;
import com.epam.ems.jwt.provider.JwtTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(JwtTokenFilter.class);
    private static final String VERIFY_TOKEN_CODE = "40303";
    private static final String TOKEN_IS_NULL_CODE = "40304";
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.verifyToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                throw new JwtAuthenticationException(TOKEN_IS_NULL_CODE, HttpStatus.UNAUTHORIZED);
            }
        } catch (JWTVerificationException e) {
            ExceptionWithCodeResponse exceptionWithCodeResponse =
                    new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(VERIFY_TOKEN_CODE), VERIFY_TOKEN_CODE, HttpStatus.UNAUTHORIZED);
            request.setAttribute("verificationException", exceptionWithCodeResponse);
        } catch (JwtAuthenticationException e) {
            ExceptionWithCodeResponse exceptionWithCodeResponse =
                    new ExceptionWithCodeResponse(MessageProvider.getLocalizedExceptionMessage(TOKEN_IS_NULL_CODE), TOKEN_IS_NULL_CODE, e.getHttpStatus());
            request.setAttribute("tokenException",exceptionWithCodeResponse);
        }
        filterChain.doFilter(request, response);
    }
}
