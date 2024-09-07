package com.springboot.spring_boot_project.service;

import com.springboot.spring_boot_project.dto.request.AssignmentCreationRequest;
import com.springboot.spring_boot_project.dto.request.AssignmentUpdateRequest;
import com.springboot.spring_boot_project.entity.Assignment;
import com.springboot.spring_boot_project.entity.Fresher;
import com.springboot.spring_boot_project.exception.AppException;
import com.springboot.spring_boot_project.exception.ErrorCode;
import com.springboot.spring_boot_project.repository.AssignmentRepository;
import com.springboot.spring_boot_project.repository.FresherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private FresherRepository fresherRepository;

    private void calculateFinalScore(Assignment assignment){
        double finalScore = (assignment.getScore1() + assignment.getScore2() +assignment.getScore3()) /3.0;
        finalScore = Math.round(finalScore * 100.0) / 100.0;
        assignment.setFinalScore(finalScore);
    }

    public Assignment createAssignment(AssignmentCreationRequest request){
        Fresher fresher = fresherRepository.findById(request.getFresher_id())
                .orElseThrow(()-> new AppException(ErrorCode.FRESHER_NOT_EXISTED));
        Assignment assignment = new Assignment();
        assignment.setFresher(fresher);
        assignment.setScore1(request.getScore1());
        assignment.setScore2(request.getScore2());
        assignment.setScore3(request.getScore3());

        calculateFinalScore(assignment);
        return assignmentRepository.save(assignment);
    }
    public List<Assignment> getAllScore(){
        return assignmentRepository.findAll();
    }
    public Assignment getScoreByFresherId(int fresherId){
        if (!fresherRepository.existsById(fresherId)){
            throw new AppException(ErrorCode.FRESHER_NOT_EXISTED);
        }
        Assignment assignment = assignmentRepository.findByFresherId(fresherId);

        if(assignment == null){
            throw new AppException(ErrorCode.SCORE_NOT_FOUND);
        }
        return assignment;
    }
    public Assignment updateAssignment(AssignmentUpdateRequest request, int fresherId){
        Assignment assignment = getScoreByFresherId(fresherId);
        assignment.setScore1(request.getScore1());
        assignment.setScore2(request.getScore2());
        assignment.setScore3(request.getScore3());
        return assignmentRepository.save(assignment);
    }

}
