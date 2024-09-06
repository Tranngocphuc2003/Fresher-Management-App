package com.springboot.spring_boot_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotNull(message = "Score1 cannot be null")
    @Min(value = 0, message = "Score1 must be at least 0")
    @Max(value = 100, message = "Score1 must be at most 100")
    @Column(nullable = false)
    double score1;
    @NotNull(message = "Score2 cannot be null")
    @Min(value = 0, message = "Score2 must be at least 0")
    @Max(value = 100, message = "Score2 must be at most 100")
    @Column(nullable = false)
    double score2;
    @NotNull(message = "Score3 cannot be null")
    @Min(value = 0, message = "Score3 must be at least 0")
    @Max(value = 100, message = "Score3 must be at most 100")
    @Column(nullable = false)
    double score3;
    @Column(name = "final_score")
    double finalScore;

    @OneToOne
    @JoinColumn(name = "fresher_id", nullable = false, referencedColumnName = "id")
    Fresher fresher;
    @PrePersist
    @PreUpdate
    @PostLoad
    public void calculateFinalScore(){
        this.finalScore = Math.round(((score1 + score2 + score3) / 3.0) * 100.0) / 100.0;
    }


}
