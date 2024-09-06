package com.springboot.spring_boot_project.service;
import com.springboot.spring_boot_project.dto.request.CenterCreationRequest;
import com.springboot.spring_boot_project.dto.request.CenterUpdateRequest;
import com.springboot.spring_boot_project.entity.Center;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.CenterRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class CenterService {
    private CenterRepository centerRepository;
    public Center createCenter(CenterCreationRequest request){
        Center center = new Center();
        if (centerRepository.existsByNameAndLocation(request.getName(), request.getLocation())){
            throw new AppException(ErrorCode.CENTER_EXISTED);
        }
        center.setName(request.getName());
        center.setLocation(request.getLocation());
        return centerRepository.save(center);
    }
    public List<Center> getCenters(){
        return centerRepository.findAll();
    }
    public Center getCenter(int id){
        return centerRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CENTER_NOT_FOUND));
    }
    public Center updateCenter(int centerId, CenterUpdateRequest request){
        Center center = centerRepository.findById(centerId).orElseThrow(()-> new AppException((ErrorCode.CENTER_NOT_FOUND)));

        center.setName(request.getName());
        center.setLocation(request.getLocation());
        return centerRepository.save(center);
    }
    public void deleteCenter(int centerId){
        Center center = centerRepository.findById(centerId).orElseThrow(()-> new AppException((ErrorCode.CENTER_NOT_FOUND)));
        centerRepository.deleteById(centerId);
    }
}
