package com.springboot.spring_boot_project.repository;

import com.springboot.spring_boot_project.entity.Fresher;
import com.springboot.spring_boot_project.entity.FresherProject;
import com.springboot.spring_boot_project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FresherProjectRepository  extends JpaRepository<FresherProject, Integer> {
    List<FresherProject> findByFresher(Fresher fresher);


    FresherProject findByFresherAndProject( Fresher fresher,Project project);
    @Modifying
    @Transactional
    @Query("DELETE FROM FresherProject fp WHERE fp.fresher.id = :fresherId")
    void deleteByFresherId(int fresherId);
    boolean existsByFresherIdAndStatus(int fresherId, String status);
}
