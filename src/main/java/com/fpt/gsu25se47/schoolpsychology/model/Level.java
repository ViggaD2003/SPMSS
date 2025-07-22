package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.model.enums.LevelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "levels")
@Data
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
}
