package com.springboot.spring_boot_project.repository;

import com.springboot.spring_boot_project.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {
    boolean existsByName(String name);
    Optional<UserInfo> findByName(String name);
}
