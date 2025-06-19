package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
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

    private Integer evaluationId; // reference to Appointment, Survey, or Program (depends on type)

    @Enumerated(EnumType.STRING)
    private EvaluationType evaluationType;

    private Integer totalScore;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Account student;

    @ManyToOne
    @JoinColumn(name = "appointment_record_id", nullable = false)
    private AppointmentRecord appointmentRecord;

    @ManyToOne
    @JoinColumn(name = "program_record_id", nullable = false)
    private ProgramRecord programRecord;

    @ManyToOne
    @JoinColumn(name = "survey_record_id", nullable = false)
    private SurveyRecord surveyRecord;
}
