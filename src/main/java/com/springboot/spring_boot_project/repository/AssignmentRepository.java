package com.springboot.spring_boot_project.repository;
import com.springboot.spring_boot_project.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    Assignment findByFresherId(int fresherId);
    @Query("SELECT CASE " +
            "WHEN s.finalScore >= 90 THEN '90-100' " +
            "WHEN s.finalScore >= 80 THEN '80-89' " +
            "WHEN s.finalScore >= 70 THEN '70-79' " +
            "WHEN s.finalScore >= 60 THEN '60-69' " +
            "WHEN s.finalScore >= 50 THEN '50-59' "+
            "ELSE 'Below 50' " +
            "END AS scoreRange, COUNT(s) " +
            "FROM Assignment s " +
            "GROUP BY CASE " +
            "WHEN s.finalScore >= 90 THEN '90-100' " +
            "WHEN s.finalScore >= 80 THEN '80-89' " +
            "WHEN s.finalScore >= 70 THEN '70-79' " +
            "WHEN s.finalScore >= 60 THEN '60-69' " +
            "WHEN s.finalScore >= 50 THEN '50-59' "+
            "ELSE 'Below 50' " +
            "END")
    List<Object[]> countFresherByScore();

    @Modifying
    @Transactional
    @Query("DELETE FROM Assignment a WHERE a.fresher.id = :fresherId")
    void deleteByFresherId(int fresherId);
}
