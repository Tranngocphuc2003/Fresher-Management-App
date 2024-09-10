package com.springboot.spring_boot_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class FresherProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "fresher_id", nullable = false)

    private Fresher fresher;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)

    private Project project;

    private String status; // active or removed
}
