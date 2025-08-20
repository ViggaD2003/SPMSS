package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordIdentify;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyRecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private LocalDateTime completedAt;

    private Boolean isSkipped;

    @Enumerated(EnumType.STRING)
    private SurveyRecordType surveyRecordType;

    @Enumerated(EnumType.STRING)
    private SurveyRecordIdentify surveyRecordIdentify;

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
