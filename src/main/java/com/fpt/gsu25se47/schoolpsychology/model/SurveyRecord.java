package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
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
public class SurveyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Float totalScore;

    private LocalDate completedAt;

    private Boolean isSkipped;

    private SurveyRecordType surveyRecordType;

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
