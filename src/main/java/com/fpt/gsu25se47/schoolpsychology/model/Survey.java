package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.utils.GradeConverter;
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

    private Boolean isUsed;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    @Enumerated(EnumType.STRING)
    private RecurringCycle recurringCycle;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private TargetScope targetScope;

    @Convert(converter = GradeConverter.class)
    @Column(columnDefinition = "TEXT") // hoặc VARCHAR nếu chỉ 1-2 giá trị
    private List<Grade> targetGradeLevel;

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
