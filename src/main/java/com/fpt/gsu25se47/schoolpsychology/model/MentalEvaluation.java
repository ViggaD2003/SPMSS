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

    @Enumerated(EnumType.STRING)
    private EvaluationType evaluationType;

    private Integer totalScore;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @OneToOne
    @JoinColumn(name = "appointment_record_id")
    private AppointmentRecord appointmentRecord;

    @OneToOne
    @JoinColumn(name = "program_record_id")
    private ProgramRecord programRecord;

    @OneToOne
    @JoinColumn(name = "survey_record_id")
    private SurveyRecord surveyRecord;
}
