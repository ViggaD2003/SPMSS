package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    private String description;

    private Boolean isSum;

    private Boolean isLimited;

    private Integer questionLength;

    private Float severityWeight;

    private Boolean isActive;

    private Integer maxScore;

    private Integer minScore;

    @OneToMany(mappedBy = "category")
    private List<SupportProgram> supportPrograms = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Survey> surveys = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Level> levels = new ArrayList<>();
}
