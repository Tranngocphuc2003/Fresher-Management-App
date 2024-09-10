package com.springboot.spring_boot_project.controller;

import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.FresherCreationRequest;
import com.springboot.spring_boot_project.dto.request.FresherUpdateRequest;
import com.springboot.spring_boot_project.entity.Fresher;
import com.springboot.spring_boot_project.entity.FresherProject;
import com.springboot.spring_boot_project.entity.Project;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.service.FresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/fresher")
public class FresherController {
    @Autowired
    private FresherService fresherService;
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    ApiResponse<Fresher> createFresher(@RequestBody FresherCreationRequest request){
        Fresher createFresher = fresherService.createFresher(request);
        ApiResponse<Fresher> apiResponse = new ApiResponse<>(200, "Create fresher successfully", createFresher);
        return apiResponse;
    }
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping
    ApiResponse<Map<String,Object>> getFreshers(){
        List<Fresher> getFreshers =  fresherService.getFreshers();
        long count = fresherService.getFresherCount();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("count", count);
        response.put("freshers", getFreshers);


        ApiResponse<Map<String,Object>> apiResponse = new ApiResponse<>(200, "Get all freshers successfully", response);
        return apiResponse;
    }
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/{fresherId}")
    ApiResponse<Fresher> getFresherById(@PathVariable("fresherId") int fresherID){
        try {
            Fresher getFresher = fresherService.getFresherById(fresherID);
            ApiResponse<Fresher> apiResponse = new ApiResponse<>(200, "Get fresher successfully ", getFresher);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/name/{name}")
    public ApiResponse<List<Fresher>> getFresherByName(@PathVariable("name") String name){
        List<Fresher> freshers = fresherService.searchByName(name);
        if(freshers.isEmpty()){
            return new ApiResponse<>(404, "No freshers found with the given name", null);
        }
        return new ApiResponse<>(200, "Fresher found", freshers);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/email/{email}")
    public ApiResponse<List<Fresher>> getFresherByEmail(@PathVariable("email") String email){
        List<Fresher> freshers = fresherService.searchByEmail(email);
        if(freshers.isEmpty()){
            return new ApiResponse<>(404, "No freshers found with the given email", null);
        }
        return new ApiResponse<>(200, "Fresher found", freshers);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/programming_language/{programming_language}")
    public ApiResponse<List<Fresher>> getFresherByProgrammingLanguage(
            @PathVariable("programming_language") String programmingLanguage)
    {
        List<Fresher> freshers = fresherService.searchByProgrammingLanguage(programmingLanguage);
        if(freshers.isEmpty()){
            return new ApiResponse<>(404, "No freshers found with the given programming language", null);
        }
        return new ApiResponse<>(200, "Fresher found", freshers);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{fresherId}")
    ApiResponse<Fresher> updateFresher(@PathVariable int fresherId, @RequestBody FresherUpdateRequest request){
        try {
            Fresher updateFresher = fresherService.updateFresher(fresherId, request);
            ApiResponse<Fresher> apiResponse = new ApiResponse<>(200, "Update fresher successfully", updateFresher);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{fresherId}")
    ApiResponse<Void> deleteFresher(@PathVariable("fresherId") int fresherId){
        try{
            fresherService.deleteFresher(fresherId);
            ApiResponse<Void> apiResponse = new ApiResponse<>(200, "Delete fresher successfully", null);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("#fresherId == principal.getFresherId()")
    @GetMapping("/{fresherId}/projects")
    public ApiResponse<List<FresherProject>> getFresherProjects(@PathVariable("fresherId") int fresherId){
        try {
            List<FresherProject> projects = fresherService.getProjectsByFresherId(fresherId);
            return new ApiResponse<>(200, "Project found", projects);
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/center/{centerId}")
    public ApiResponse<List<Fresher>> getFreshersByCenter(@PathVariable("centerId") int centerId){
        try {
            List<Fresher> freshers = fresherService.getFreshersByCenterId(centerId);
            return new ApiResponse<>(200, "Fresher found", freshers);
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{fresherId}/center/{centerId}")
    public ApiResponse<String> addFresherToCenter(@PathVariable("fresherId") int fresherId, @PathVariable("centerId") int centerId){
        try {
            fresherService.addFresherToCenter(fresherId, centerId);
            return new ApiResponse<>(200,"Added fresher to center successfully", null);
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }



}
