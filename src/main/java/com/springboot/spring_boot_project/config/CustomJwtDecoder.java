package com.springboot.spring_boot_project.config;

import com.springboot.spring_boot_project.dto.request.IntrospectRequest;
import com.springboot.spring_boot_project.service.AuthenticationService;
import com.springboot.spring_boot_project.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder{
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;

    public Claims decode(String token) throws JwtException {

        try {
            // Introspect the token using the authentication service
            var response = authenticationService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build());

            if (!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }

        try {
            // Decode and validate the token using jsonwebtoken
            return Jwts.parser()
                    .setSigningKey(jwtService.getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.JwtException e) {
            throw new JwtException("Token parsing failed: " + e.getMessage());
        }
    }
}

