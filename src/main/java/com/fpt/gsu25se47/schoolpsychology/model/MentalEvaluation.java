package com.fpt.gsu25se47.schoolpsychology.model;

import com.assignment.test.model.enums.Source;
import com.assignment.test.model.enums.SourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mental_evaluation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentalEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Source source;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    private Float weightedScore;

    private LocalDate firstEvaluatedAt;

    private LocalDate lastEvaluatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_record_id", referencedColumnName = "id")
    private SurveyRecord surveyRecord;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;
}
