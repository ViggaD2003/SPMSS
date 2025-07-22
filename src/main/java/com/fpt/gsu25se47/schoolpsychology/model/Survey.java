package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.common.Auditable;
import com.assignment.test.model.enums.Grade;
import com.assignment.test.model.enums.SurveyStatus;
import com.assignment.test.model.enums.TargetScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Boolean isRequired;

    private Boolean isRecurring;

    private Integer round;

    private String surveyCode;

    private String recurringCycle; // e.g., "WEEKLY", "MONTHLY", etc.

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TargetScope targetScope;

    private Grade targetGradeLevel;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    @OneToMany(mappedBy = "survey")
    private List<SurveyCaseLink> surveyCaseLinks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account createBy;

    @OneToMany(mappedBy = "survey")
    private List<SurveyRecord> surveyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();
}
