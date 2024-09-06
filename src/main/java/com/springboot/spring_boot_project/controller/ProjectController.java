package com.springboot.spring_boot_project.controller;
import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.ProjectCreationRequest;
import com.springboot.spring_boot_project.dto.request.ProjectUpdateRequest;
import com.springboot.spring_boot_project.entity.Project;
import com.springboot.spring_boot_project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    ApiResponse<Project> createProject(@RequestBody ProjectCreationRequest request){
        Project createProject = projectService.createProject(request);
        ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Create project successfully", createProject);
        return apiResponse;
    }
    @GetMapping
    ApiResponse<List<Project>> getAllProjects() {
        List<Project> getAllProjects = projectService.getProjects();
        ApiResponse<List<Project>> apiResponse = new ApiResponse<>(200,"Get all projects successfully",getAllProjects);
        return apiResponse;
    }
    @GetMapping("/{projectId}")
    ApiResponse<Project> getProject(@PathVariable("projectId") int projectID){
        Project getProject = projectService.getProject(projectID);
        ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Get project successfully", getProject);
        return apiResponse;
    }
    @PutMapping("/{projectId}")
    ApiResponse<Project> updateProject(@PathVariable int projectId, @RequestBody ProjectUpdateRequest request){
        Project updateProject =  projectService.updateProject(projectId, request);
        ApiResponse<Project> apiResponse = new ApiResponse<>(200, "Update project successfully", updateProject);
        return apiResponse;
    }
    @DeleteMapping("/{projectId}")
    ApiResponse<Void> deleteProject(@PathVariable int projectId){
        projectService.deleteProject(projectId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(200, "Delete project successfully",null);
        return apiResponse;
    }
    @PostMapping("/{projectId}/freshers/{fresherId}")
    public String addFresherToProject(
            @PathVariable("projectId") int projectId, @PathVariable("fresherId") int fresherId)
    {
        projectService.addFresherToProject(projectId,fresherId);
        return "Fresher added to project and notification email sent";
    }
    @DeleteMapping("/{projectId}/freshers/{fresherId}")
    public String removeFresherFromProject(
            @PathVariable("projectId") int projectId, @PathVariable("fresherId") int fresherId)
    {
        projectService.removeFresherToProject(projectId,fresherId);
        return "Fresher removed from project and notification email sent";
    }
}
