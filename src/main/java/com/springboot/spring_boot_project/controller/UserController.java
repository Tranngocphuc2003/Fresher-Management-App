package com.springboot.spring_boot_project.controller;

import com.springboot.spring_boot_project.dto.request.*;
import com.springboot.spring_boot_project.dto.response.UserResponse;
import com.springboot.spring_boot_project.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping
    ApiResponse<UserResponse> createUserInfo(@RequestBody UserInfoCreationRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Create user successfully")
                .result(userInfoService.addUserInfo(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUserInfos(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .message("Get all user successfully")
                .result(userInfoService.getAllUserInfo())
                .build()
                ;
    }
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserInfo(@PathVariable("userId") String userID){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get user successfully")
                .result(userInfoService.getUserInfo(userID))
                .build();
    }
     @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUserInfo(@PathVariable String userId, @RequestBody UserInfoUpdateRequest request){
       return ApiResponse.<UserResponse>builder()
               .code(200)
               .message("Update user successfully")
               .result(userInfoService.updateUserInfo(userId,request))
               .build();
    }
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUserInfo(@PathVariable String userId){
        userInfoService.deleteUserInfo(userId);
        return ApiResponse.<String>builder()
                .code(200)
                .message("User has been deleted")
                .build();
    }
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get my info successfully")
                .result(userInfoService.getMyInfo())
                .build();
    }
}
