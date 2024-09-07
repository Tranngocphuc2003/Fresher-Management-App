package com.springboot.spring_boot_project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreationRequest {
    @NotNull(message = "Score1 cannot be null")
    @Min(value = 0, message = "Score1 must be at least 0")
    @Max(value = 100, message = "Score1 must be at most 100")
    double score1;
    @NotNull(message = "Score2 cannot be null")
    @Min(value = 0, message = "Score2 must be at least 0")
    @Max(value = 100, message = "Score2 must be at most 100")
    double score2;
    @NotNull(message = "Score3 cannot be null")
    @Min(value = 0, message = "Score3 must be at least 0")
    @Max(value = 100, message = "Score3 must be at most 100")
    double score3;
    int fresher_id;
}
