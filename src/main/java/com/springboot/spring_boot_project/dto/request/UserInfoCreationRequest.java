package com.springboot.spring_boot_project.dto.request;

import com.springboot.spring_boot_project.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoCreationRequest {
    String name;
    String email;
    String password;
}
