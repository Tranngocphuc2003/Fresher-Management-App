package com.springboot.spring_boot_project.config;

import com.springboot.spring_boot_project.entity.Role;
import com.springboot.spring_boot_project.entity.UserInfo;
import com.springboot.spring_boot_project.repository.UserInfoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashSet;
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ApplicationConfig {
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);
    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner applicationRunner(UserInfoRepository userInfoRepository){
        return args -> {
            if(userInfoRepository.findByName("admin").isEmpty()){
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());

                UserInfo userInfo = UserInfo.builder()
                        .name("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();
                userInfoRepository.save(userInfo);
                log.warn("admin user has been created with default password: admin, please change it");

            }
            log.info("Application initialization completed  ");
        };
    }
}
