package com.springboot.spring_boot_project.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Project", schema ="fresher_management")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     int id;
    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "manager", nullable = false)
    String manager;
    @Column(name = "startDate", nullable = false)
    LocalDate startDate;
    @Column(name = "endDate")
    LocalDate endDate;
    @Column(name = "language", nullable = false)
    String language;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;
    public enum Status{
        NOT_STARTED,
        ONGOING,
        CANCELED,
        CLOSED
    }

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false, referencedColumnName = "id")
    Center center;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "fresher_project",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "fresher_id")
    )
    Set<Fresher> freshers = new HashSet<>();

}
