package com.springboot.spring_boot_project.service;

import com.springboot.spring_boot_project.dto.request.ApiResponse;
import com.springboot.spring_boot_project.dto.request.FresherCreationRequest;
import com.springboot.spring_boot_project.dto.request.FresherUpdateRequest;
import com.springboot.spring_boot_project.entity.*;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class FresherService {
    private UserInfoRepository userInfoRepository;
    private FresherRepository fresherRepository;

    private AssignmentRepository assignmentRepository;

    private CenterRepository centerRepository;

    private FresherProjectRepository fresherProjectRepository;
    private ProjectService projectService;
    public Fresher createFresher(FresherCreationRequest request){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userInfoRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Fresher fr = new Fresher();
        fr.setName(request.getName());
        fr.setEmail(request.getEmail());
        fr.setProgrammingLanguage(request.getProgramming_language());
        fr.setUser(userInfo);
        return fresherRepository.save(fr);
    }
    public List<Fresher> getFreshers(){
        return fresherRepository.findAll();
    }
    public Fresher getFresherById(int id){
        return fresherRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.FRESHER_NOT_EXISTED));
    }
    public List<Fresher> searchByName(String name){
        return fresherRepository.findByName(name);
    }
    public List<Fresher> searchByProgrammingLanguage(String programmingLanguage){
        return fresherRepository.findByProgrammingLanguage(programmingLanguage);
    }
    public List<Fresher> searchByEmail(String email){
        return fresherRepository.findByEmail(email);
    }
    public Fresher updateFresher(int fresherId, FresherUpdateRequest request){
        Fresher fr = getFresherById(fresherId);

        fr.setEmail(request.getEmail());
        fr.setProgrammingLanguage(request.getProgramming_language());

        return fresherRepository.save(fr);
    }
    public void deleteFresher(int fresherId){
        Fresher fresher = getFresherById(fresherId);
        if (projectService.isFresherInAnyProject(fresherId)) {
            throw new AppException(ErrorCode.FRESHER_STILL_IN_PROJECT);
        }
        fresherProjectRepository.deleteByFresherId(fresherId);
        assignmentRepository.deleteByFresherId(fresherId);
        fresherRepository.delete(fresher);
    }
    public List<FresherProject> getProjectsByFresherId(int fresherId){
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(()-> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        return fresherProjectRepository.findByFresher(fresher);
    }
    public Fresher addFresherToCenter(int fresherId, int centerId){
        Fresher fresher = fresherRepository.findById(fresherId)
                .orElseThrow(()->new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        Center center = centerRepository.findById(centerId)
                .orElseThrow(()->new AppException(ErrorCode.CENTER_NOT_FOUND));

        fresher.setCenter(center);
        return fresherRepository.save(fresher);
    }

    public long getFresherCount(){
        return fresherRepository.count();
    }
    public List<Fresher> getFreshersByCenterId(int centerId){
        if(centerRepository.findById(centerId).isEmpty()){
            throw new AppException(ErrorCode.CENTER_NOT_FOUND);
        }
        return fresherRepository.findByCenterId(centerId);
    }
    public Map<String, Long> getFresherCountByScore() {
        List<Object[]> results = assignmentRepository.countFresherByScore();
        for (Object[] result : results) {
            System.out.println("Range score : " + result[0] + ", Count: " + result[1]);
        }
        return results.stream().collect(Collectors.toMap(
                result -> (String) result[0],  // range score
                result -> (Long) result[1]     // fresher count
        ));
    }
    public Map<String, Long> getFresherCountByCenter() {
        List<Object[]> results = fresherRepository.countFresherByCenter();
        for (Object[] result : results) {
            System.out.println("Center: " + result[0] + ", Count: " + result[1]);
        }
        return results.stream().collect(Collectors.toMap(
                result -> (String) result[0],  // Center name
                result -> (Long) result[1]     // Fresher count
        ));
    }
}
