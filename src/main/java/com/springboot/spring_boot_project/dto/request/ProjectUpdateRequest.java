package com.springboot.spring_boot_project.dto.request;

import com.springboot.spring_boot_project.entity.Project;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectUpdateRequest {
    String manager;
    LocalDate endDate;
    String language;
    String center;
    Project.Status status;
}
