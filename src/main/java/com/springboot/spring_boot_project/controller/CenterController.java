package com.springboot.spring_boot_project.controller;
import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.CenterCreationRequest;
import com.springboot.spring_boot_project.dto.request.CenterUpdateRequest;
import com.springboot.spring_boot_project.entity.Center;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.service.CenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/center")
public class CenterController {
    @Autowired
    private CenterService centerService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    ApiResponse<Center> createCenter(@RequestBody CenterCreationRequest request){
        Center createdCenter = centerService.createCenter(request);
        ApiResponse<Center> apiResponse = new ApiResponse<>(200, "Create center successfully",createdCenter);
        return apiResponse;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    ApiResponse<List<Center>> getCenter(){
        List<Center> getAllCenter = centerService.getCenters();
        ApiResponse<List<Center>> apiResponse = new ApiResponse<>(200, "Get all centers successfully", getAllCenter);
        return apiResponse;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{centerId}")
    ApiResponse <Center> getCenter(@PathVariable("centerId") int centerID){
        try {
            Center getCenter = centerService.getCenter(centerID);
            ApiResponse<Center> apiResponse = new ApiResponse<>(200, "Get center succesfully", getCenter);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{centerId}")
    ApiResponse<Center> updateCenter(@PathVariable("centerId") int centerId, @RequestBody CenterUpdateRequest request){
        try {
            Center updateCenter = centerService.updateCenter(centerId, request);
            ApiResponse<Center> apiResponse = new ApiResponse<>(200, "Update center successfully", updateCenter);
            return apiResponse;
        }catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{centerId}")
    ApiResponse<Void> deleteCenter(@PathVariable("centerId") int centerId){
        try{
            centerService.deleteCenter(centerId);
            ApiResponse<Void> apiResponse = new ApiResponse<>(200, "Delete center successfully", null);
            return apiResponse;
        }catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(), e.getErrorCode().getMessage(), null);
        }
    }
}
