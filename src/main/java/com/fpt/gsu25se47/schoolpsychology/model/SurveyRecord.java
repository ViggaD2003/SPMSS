package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String noteSuggest;

    private Integer totalScore;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    private LocalDate completedAt;

    private LocalDate createdAt;

    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "surveyRecord")
    private List<MentalEvaluation> mentalEvaluations = new ArrayList<>();

    @OneToOne(mappedBy = "surveyRecord")
    private AnswerRecord answerRecord;
}
