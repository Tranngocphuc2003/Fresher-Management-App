package com.springboot.spring_boot_project.dto.request;
import com.springboot.spring_boot_project.entity.Project.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectCreationRequest {
    String name;
    String manager;
    String center;
    LocalDate startDate;
    LocalDate endDate;
    String language;
    Status status;
}
