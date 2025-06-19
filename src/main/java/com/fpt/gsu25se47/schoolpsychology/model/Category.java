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
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    private LocalDate createAt;

    private LocalDate updateAt;

    @OneToMany(mappedBy = "category")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<SupportProgram> supportPrograms = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<MentalEvaluation> mentalEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Level> levels = new ArrayList<>();
}
