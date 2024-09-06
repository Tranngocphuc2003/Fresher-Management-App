package com.springboot.spring_boot_project.exception;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AppException extends RuntimeException {
    ErrorCode errorCode;
}
