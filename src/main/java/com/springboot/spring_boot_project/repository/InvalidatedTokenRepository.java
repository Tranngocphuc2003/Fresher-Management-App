package com.springboot.spring_boot_project.repository;

import com.springboot.spring_boot_project.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}