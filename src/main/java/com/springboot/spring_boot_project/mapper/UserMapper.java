package com.springboot.spring_boot_project.mapper;

import com.springboot.spring_boot_project.dto.request.UserInfoCreationRequest;
import com.springboot.spring_boot_project.dto.request.UserInfoUpdateRequest;
import com.springboot.spring_boot_project.dto.response.UserResponse;
import com.springboot.spring_boot_project.entity.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfo toUser(UserInfoCreationRequest request);

    UserResponse toUserResponse(UserInfo user);

    void updateUser(@MappingTarget UserInfo user, UserInfoUpdateRequest request);
}
