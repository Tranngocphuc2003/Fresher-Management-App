package com.springboot.spring_boot_project.controller;
import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.ProjectCreationRequest;
import com.springboot.spring_boot_project.dto.request.ProjectUpdateRequest;
import com.springboot.spring_boot_project.entity.Project;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    ApiResponse<Project> createProject(@RequestBody ProjectCreationRequest request){
        try {
            Project createProject = projectService.createProject(request);
            ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Create project successfully", createProject);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    ApiResponse<List<Project>> getAllProjects() {
        List<Project> getAllProjects = projectService.getProjects();
        ApiResponse<List<Project>> apiResponse = new ApiResponse<>(200,"Get all projects successfully",getAllProjects);
        return apiResponse;
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{projectId}")
    ApiResponse<Project> getProject(@PathVariable("projectId") int projectID){
        try {
            Project getProject = projectService.getProject(projectID);
            ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Get project successfully", getProject);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{projectId}")
    ApiResponse<Project> updateProject(@PathVariable int projectId, @RequestBody ProjectUpdateRequest request){
        try {
            Project updateProject = projectService.updateProject(projectId, request);
            ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Update project successfully", updateProject);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{projectId}")
    ApiResponse<Void> deleteProject(@PathVariable("projectId") int projectId){
        try {
            projectService.deleteProject(projectId);
            ApiResponse<Void> apiResponse = new ApiResponse<>(200, "Delete project successfully", null);
            return apiResponse;
        } catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(),e.getErrorCode().getMessage(),null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{projectId}/freshers/{fresherId}")
    public ApiResponse<Void> addFresherToProject(
            @PathVariable("projectId") int projectId, @PathVariable("fresherId") int fresherId) {
        try {
            projectService.addFresherToProject(projectId, fresherId);
            return new ApiResponse<>(200, "Fresher added to project and notification email sent", null);
        }catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(), e.getErrorCode().getMessage(), null);
        }
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{projectId}/freshers/{fresherId}")
    public ApiResponse<Void> removeFresherFromProject(
            @PathVariable("projectId") int projectId, @PathVariable("fresherId") int fresherId)
    {
        try {
            projectService.removeFresherFromProject(projectId, fresherId);
            return new ApiResponse<>(200, "Fresher removed to project and notification email sent", null);
        }catch (AppException e){
            return new ApiResponse<>(e.getErrorCode().getCode(), e.getErrorCode().getMessage(), null);
        }
    }
}
