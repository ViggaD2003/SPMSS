package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.common.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    private BigDecimal totalScore;

    private LocalDate completedAt;

    private Integer round;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account student;

    @OneToMany(mappedBy = "surveyRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerRecord> answerRecords = new ArrayList<>();

    @OneToOne(mappedBy = "surveyRecord")
    private MentalEvaluation mentalEvaluation;
}
