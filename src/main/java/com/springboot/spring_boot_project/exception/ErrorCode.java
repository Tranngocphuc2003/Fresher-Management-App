package com.springboot.spring_boot_project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    CENTER_EXISTED(1001, "Center existed",HttpStatus.FOUND),
    CENTER_NOT_FOUND(1002, "Center not found", HttpStatus.NOT_FOUND),
    PROJECT_NOT_FOUND(1003,"Project not found",HttpStatus.NOT_FOUND),
    USER_EXISTED(1004,"User existed",HttpStatus.FOUND),
    USER_NOT_EXISTED(1005,"User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    FRESHER_NOT_EXISTED(1007, "Fresher not existed",HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1008, "You do not have permission",HttpStatus.FORBIDDEN);

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
