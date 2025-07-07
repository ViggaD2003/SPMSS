package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false, unique = true)
    private String studentCode;

    private Boolean isEnableSurvey;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Classes classes;

    @OneToMany(mappedBy = "student")
    private List<Relationship> relationships = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<MentalEvaluation> mentalEvaluations = new ArrayList<>();
}