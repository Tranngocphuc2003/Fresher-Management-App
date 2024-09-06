package com.springboot.spring_boot_project.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Fresher",
        schema = "fresher_management"
        )
public class Fresher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "email", nullable = false)
    String email;
    @Column(name = "programmingLanguage",nullable = false)
    String programmingLanguage;
    @ManyToOne
    @JoinColumn(name = "center_id", nullable = true,referencedColumnName = "id")
    Center center;
    @ManyToMany(mappedBy = "freshers")
    @JsonIgnore
    Set<Project> projects = new HashSet<>();
}
