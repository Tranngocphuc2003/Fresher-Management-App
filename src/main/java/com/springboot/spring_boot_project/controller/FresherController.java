package com.springboot.spring_boot_project.controller;

import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.FresherCreationRequest;
import com.springboot.spring_boot_project.dto.request.FresherUpdateRequest;
import com.springboot.spring_boot_project.entity.Fresher;
import com.springboot.spring_boot_project.entity.Project;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.service.FresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/fresher")
public class FresherController {
    @Autowired
    private FresherService fresherService;

    @PostMapping
    ApiResponse<Fresher> createFresher(@RequestBody FresherCreationRequest request){
        Fresher createFresher = fresherService.createFresher(request);
        ApiResponse<Fresher> apiResponse = new ApiResponse<>(200, "Create fresher successfully", createFresher);
        return apiResponse;
    }
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
    @GetMapping("/name/{name}")
    public ApiResponse<List<Fresher>> getFresherByName(@PathVariable("name") String name){
        List<Fresher> freshers = fresherService.searchByName(name);
        if(freshers.isEmpty()){
            return new ApiResponse<>(404, "No freshers found with the given name", null);
        }
        return new ApiResponse<>(200, "Fresher found", freshers);
    }
    @GetMapping("/email/{email}")
    public ApiResponse<List<Fresher>> getFresherByEmail(@PathVariable("email") String email){
        List<Fresher> freshers = fresherService.searchByEmail(email);
        if(freshers.isEmpty()){
            return new ApiResponse<>(404, "No freshers found with the given email", null);
        }
        return new ApiResponse<>(200, "Fresher found", freshers);
    }
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
    @PutMapping("/{fresherId}")
    ApiResponse<Fresher> updateFresher(@PathVariable int fresherId, @RequestBody FresherUpdateRequest request){
        Fresher updateFresher = fresherService.updateFresher(fresherId, request);
        ApiResponse<Fresher> apiResponse = new ApiResponse<>(200, "Update fresher successfully",updateFresher);
        return apiResponse;
    }

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
    @GetMapping("/{fresherId}/projects")
    public ApiResponse<Set<Project>> getFresherProjects(@PathVariable("fresherId") int fresherId){
        Set<Project> projects = fresherService.getProjectsByFresherId(fresherId);
        return new ApiResponse<>(200, "Project found",projects);
    }
    @GetMapping("/center/{centerId}")
    public List<Fresher> getFreshersByCenter(@PathVariable("centerId") int centerId){
        return fresherService.getFreshersByCenterId(centerId);
    }
    @PutMapping("/{fresherId}/center/{centerId}")
    public Fresher addFresherToCenter(@PathVariable("fresherId") int fresherId, @PathVariable("centerId") int centerId){
        return fresherService.addFresherToCenter(fresherId,centerId);
    }



}
