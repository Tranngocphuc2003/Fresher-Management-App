package com.springboot.spring_boot_project.service;

import com.springboot.spring_boot_project.dto.request.AuthenticationRequest;
import com.springboot.spring_boot_project.dto.request.IntrospectRequest;
import com.springboot.spring_boot_project.dto.request.LogoutRequest;
import com.springboot.spring_boot_project.dto.request.RefreshRequest;
import com.springboot.spring_boot_project.dto.response.AuthenticationResponse;
import com.springboot.spring_boot_project.dto.response.IntrospectResponse;
import com.springboot.spring_boot_project.entity.InvalidatedToken;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.InvalidatedTokenRepository;
import com.springboot.spring_boot_project.repository.UserInfoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserInfoRepository userInfoRepository;
    JwtService jwtService;
    InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticationResponse login(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userInfoRepository.findByName(request.getName()).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        var token = jwtService.generateToken(user);
        if (!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
public IntrospectResponse introspect(IntrospectRequest request) {
    var token = request.getToken();
    boolean isValid = true;

    try {
        jwtService.verifyToken(token,true);
    } catch (AppException e) {
        isValid = false;
    }

    return IntrospectResponse.builder()
            .valid(isValid)
            .build();
}
    public void logout(LogoutRequest request){
        try {
            var signToken = jwtService.verifyToken(request.getToken(), false);
            String jit = signToken.getId();
            Date expiryTime = signToken.getExpiration();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException exception){
            log.info("Token already expired");
        }

    }

    public AuthenticationResponse refreshToken(RefreshRequest request){
        var signToken = jwtService.verifyToken(request.getToken(),false);
        var jit = signToken.getId();
        var expiryTime = signToken.getExpiration();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signToken.getSubject();

        var user = userInfoRepository.findByName(username)
                .orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }


}
