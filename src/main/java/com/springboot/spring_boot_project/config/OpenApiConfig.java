package com.springboot.spring_boot_project.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl) {
        return new OpenAPI().info(new Info().title(title)
                        .version(version)
                        .description("API description")
                        .license(new License().name("API license").url("https://springdoc.org")))
                .servers(List.of(new Server().url(serverUrl)));
    }
        @Bean
        public GroupedOpenApi groupedOpenApi() {
            return GroupedOpenApi.builder()
                    .group("fresher-management service")
                    .packagesToScan("com.springboot.spring_boot_project.controller")
                    .build();
        }
    }

