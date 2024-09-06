package com.springboot.spring_boot_project.service;

import com.springboot.spring_boot_project.entity.UserInfo;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {
    InvalidatedTokenRepository invalidatedTokenRepository;

    // Replace this with a secure key in a real application, ideally fetched from environment variables
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected Long VALID_DURATION ;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected Long REFRESHABLE_DURATION ;


    // Create a JWT token with specified claims and subject (user name)
    public String generateToken(UserInfo userInfo) {
        String token =  Jwts
                .builder()
                .claim("scope", buildScope(userInfo))
                .setSubject(userInfo.getName())
                .setIssuer("phuc")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .id(UUID.randomUUID().toString())
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    // Get the signing key for JWT token
    public Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private String buildScope(UserInfo userInfo){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(userInfo.getRoles())){
            userInfo.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }
    public Claims verifyToken(String token, boolean isRefresh) {
            // Parse and validate the token
            Claims claims = extractAllClaims(token);

            // Check the expiration date
            Date expiryTime =(isRefresh)
                        ? new Date(claims.getIssuedAt().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                        : claims.getExpiration();
            if (expiryTime.before(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            // Check if the token has been invalidated
            if (invalidatedTokenRepository.existsById(claims.getId())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return claims;
        }
    }

