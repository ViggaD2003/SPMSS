package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.LevelType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String label;

    private String code;

    private Float minScore;

    private Float maxScore;

    private LevelType levelType;

    private String description;

    private String symptomsDescription;

    private String interventionRequired;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "currentLevel", cascade = CascadeType.ALL)
    private List<Cases> levelOfCurrentCase = new ArrayList<>();

    @OneToMany(mappedBy = "initialLevel", cascade = CascadeType.ALL)
    private List<Cases> levelOfInitCase = new ArrayList<>();

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL)
    private List<SurveyRecord> surveyRecords = new ArrayList<>();
}
