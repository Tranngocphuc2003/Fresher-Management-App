package com.springboot.spring_boot_project.repository;

import com.springboot.spring_boot_project.entity.Fresher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FresherRepository extends JpaRepository<Fresher, Integer > {
    boolean existsByEmail(String email);
    List<Fresher> findByName(String name);
    List<Fresher> findByEmail(String email);
    List<Fresher> findByProgrammingLanguage(String programmingLanguage);
    List<Fresher> findByCenterId(int centerId);
    @Override
    long count();
    @Query("SELECT f.center.name, COUNT(f) FROM Fresher f GROUP BY f.center.name")
    List<Object[]> countFresherByCenter();
    @Modifying
    @Transactional
    @Query("UPDATE Fresher f SET f.center = null WHERE f.center.id = :centerId")
    void clearCenterIdForFreshers(@Param("centerId") int centerId);
}
