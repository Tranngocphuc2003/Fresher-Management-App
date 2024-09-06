package com.springboot.spring_boot_project.service;

import com.springboot.spring_boot_project.dto.request.UserInfoCreationRequest;
import com.springboot.spring_boot_project.dto.request.UserInfoUpdateRequest;
import com.springboot.spring_boot_project.dto.response.UserResponse;
import com.springboot.spring_boot_project.entity.Role;
import com.springboot.spring_boot_project.entity.UserInfo;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.mapper.UserMapper;
import com.springboot.spring_boot_project.repository.UserInfoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class UserInfoService implements UserDetailsService  {

    UserInfoRepository userInfoRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse addUserInfo(UserInfoCreationRequest request){
        if (userInfoRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.USER_EXISTED);
        UserInfo userInfo = userMapper.toUser(request);
        userInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        userInfo.setRoles(roles);
        return userMapper.toUserResponse(userInfoRepository.save(userInfo));
    }
   @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponse> getAllUserInfo(){
        return userInfoRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
    @PostAuthorize("returnObject.name == authentication.name")
    public UserResponse getUserInfo(String id){
        return userMapper.toUserResponse(userInfoRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
    public UserResponse updateUserInfo(String userId, UserInfoUpdateRequest request){
        UserInfo userInfo = userInfoRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(userInfo,request);
        userInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userInfoRepository.save(userInfo));
    }
    public void deleteUserInfo(String userId){
        UserInfo userInfo = userInfoRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        userInfoRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         Optional<UserInfo> userDetail = userInfoRepository.findByName(username); // Assuming 'email' is used as username

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
    public UserResponse getMyInfo(){
        var context =  SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        UserInfo userInfo = userInfoRepository.findByName(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(userInfo);
    }

}
