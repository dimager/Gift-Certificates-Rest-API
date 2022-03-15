package com.epam.ems.jwt.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.epam.ems.exception.JwtAuthenticationException;
import com.epam.ems.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Value("${jwt.secretPhrase}")
    private String secretPhrase;
    @Value("${jwt.authHeader}")
    private String authHeader;
    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username, String role, Long id) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretPhrase);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(username)
                    .withExpiresAt(Date.from(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC)))
                    .withClaim("role", role)
                    .withClaim("id", id)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "31102;IInvalid Signing configuration / Couldn't convert Claims.");
        }
    }

    public boolean verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretPhrase);
            JWTVerifier jwtVerifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            jwtVerifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new JwtAuthenticationException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return JWT.decode(token).getSubject();
    }

    public long getId(String token) {
        return JWT.decode(token).getClaim("id").asLong();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authHeader);
    }
}
