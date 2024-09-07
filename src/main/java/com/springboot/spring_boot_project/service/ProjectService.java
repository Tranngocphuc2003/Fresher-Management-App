package com.springboot.spring_boot_project.service;
import com.springboot.spring_boot_project.dto.request.ProjectCreationRequest;
import com.springboot.spring_boot_project.dto.request.ProjectUpdateRequest;
import com.springboot.spring_boot_project.entity.Center;
import com.springboot.spring_boot_project.entity.Fresher;
import com.springboot.spring_boot_project.entity.Project;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.CenterRepository;
import com.springboot.spring_boot_project.repository.FresherRepository;
import com.springboot.spring_boot_project.repository.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class ProjectService {
    private ProjectRepository projectRepository;
    private FresherRepository fresherRepository;
    private EmailService emailService;
    private CenterRepository centerRepository;
    public Project createProject(ProjectCreationRequest request){
        Project prj = new Project();
        prj.setName(request.getName());
        prj.setLanguage(request.getLanguage());
        prj.setManager(request.getManager());
        prj.setStartDate(request.getStartDate());
        prj.setEndDate(request.getEndDate());
        prj.setStatus(request.getStatus());

        Center center = centerRepository.findByName(request.getCenter())
                .orElseThrow(()-> new AppException(ErrorCode.CENTER_NOT_FOUND));
        prj.setCenter(center);
        return projectRepository.save(prj);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
    public Project getProject(int id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found "));
    }
    public Project updateProject(int projectId,ProjectUpdateRequest request){

        Project prj = projectRepository.findById(projectId)
                .orElseThrow(()-> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        prj.setManager(request.getManager());
        prj.setLanguage(request.getLanguage());
        prj.setEndDate(request.getEndDate());
        prj.setStatus(request.getStatus());
        Center center = centerRepository.findByName(request.getCenter())
                .orElseThrow(()-> new AppException(ErrorCode.CENTER_NOT_FOUND));
        prj.setCenter(center);
        return projectRepository.save(prj);
    }
    public void deleteProject(int projectId){
        projectRepository.deleteById(projectId);
    }
    public void addFresherToProject(int projectId, int fresherId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(()->new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        project.getFreshers().add(fresher);
        fresher.getProjects().add(project);

        projectRepository.save(project);
        fresherRepository.save(fresher);
        Map<String, Object> model = new HashMap<>();
        model.put("name", fresher.getName());
        model.put("action", "added");
        model.put("projectName", project.getName());
        model.put("managerName", project.getManager());


        emailService.sendMail("tranngocphuc_t66@hus.edu.vn", "Project Notification", model, "email-template");
    }
    public void removeFresherToProject(int projectId, int fresherId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(()->new AppException(ErrorCode.FRESHER_NOT_EXISTED));

        project.getFreshers().remove(fresher);
        projectRepository.save(project);

        Map<String, Object> model = new HashMap<>();
        model.put("name", fresher.getName());
        model.put("action", "removed");
        model.put("projectName", project.getName());
        model.put("managerName", project.getManager());


        emailService.sendMail("tranngocphuc_t66@hus.edu.vn", "Project Notification", model, "email-template");
    }
    public boolean isFresherInAnyProject(int fresherId){
        return !projectRepository.findAllByFreshers_Id(fresherId).isEmpty();
    }
}
