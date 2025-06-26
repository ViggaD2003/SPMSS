package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordStatus;
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
public class SurveyRecord extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String noteSuggest;

    private Integer totalScore;

    @Enumerated(EnumType.STRING)
    private SurveyRecordStatus status;

    private LocalDate completedAt;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "mental_evaluation_id", nullable = false)
    private MentalEvaluation mentalEvaluation;

    @OneToMany(mappedBy = "surveyRecord")
    private List<AnswerRecord> answerRecords = new ArrayList<>();
}
