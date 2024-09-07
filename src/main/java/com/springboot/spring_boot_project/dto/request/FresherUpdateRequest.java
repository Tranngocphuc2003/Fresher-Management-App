package com.springboot.spring_boot_project.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FresherUpdateRequest {
    String email;
    Set<String> programming_language;
}
