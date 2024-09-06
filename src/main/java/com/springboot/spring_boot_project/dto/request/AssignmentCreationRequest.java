package com.springboot.spring_boot_project.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreationRequest {
    double score1;
    double score2;
    double score3;
    int fresher_id;
}
