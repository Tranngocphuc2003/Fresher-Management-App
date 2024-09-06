package com.springboot.spring_boot_project.dto.response;

import com.springboot.spring_boot_project.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String name;
    String email;
    Set<String> roles;
}
