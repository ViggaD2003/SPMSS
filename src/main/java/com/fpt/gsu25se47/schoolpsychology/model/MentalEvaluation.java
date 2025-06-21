package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.EvaluationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Student student;

    @OneToMany(mappedBy = "mentalEvaluation")
    private List<AppointmentRecord> appointmentRecords = new ArrayList<>();

    @OneToMany(mappedBy = "mentalEvaluation")
    private List<ProgramRecord> programRecords = new ArrayList<>();

    @OneToMany(mappedBy = "mentalEvaluation")
    private List<SurveyRecord> surveyRecords = new ArrayList<>();
}
