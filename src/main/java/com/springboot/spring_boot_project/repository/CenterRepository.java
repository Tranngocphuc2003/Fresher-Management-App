package com.springboot.spring_boot_project.repository;
import com.springboot.spring_boot_project.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CenterRepository extends JpaRepository<Center, Integer> {
    boolean existsByNameAndLocation(String name, String location);
    Optional<Center> findByName(String name);
}
