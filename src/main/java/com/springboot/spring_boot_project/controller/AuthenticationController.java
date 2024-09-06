package com.springboot.spring_boot_project.controller;

import com.springboot.spring_boot_project.dto.request.*;
import com.springboot.spring_boot_project.dto.response.AuthenticationResponse;
import com.springboot.spring_boot_project.dto.response.IntrospectResponse;
import com.springboot.spring_boot_project.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authService;


    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ){
        var result = authService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(
            @RequestBody IntrospectRequest request
    ){
        var result = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestBody LogoutRequest request
    ){
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(
            @RequestBody RefreshRequest request
    ){
        var result = authService.refreshToken(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
}
