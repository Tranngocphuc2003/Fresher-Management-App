package com.springboot.spring_boot_project.exception;

import com.springboot.spring_boot_project.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    ApiResponse HandlingRuntimeException(RuntimeException exception){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage(exception.getMessage());
        return apiResponse;
    }
    @ExceptionHandler(value = AppException.class)
    ApiResponse handlingAppException(AppException appException){
        ApiResponse apiResponse = new ApiResponse<>();
        ErrorCode errorCode = appException.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return apiResponse;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ApiResponse handlingAccessDeniedException(AccessDeniedException accessDeniedException){
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build();

    }
}
