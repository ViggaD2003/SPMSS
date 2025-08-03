package com.fpt.gsu25se47.schoolpsychology.model;


import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
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

    private Float weightedScore;

    private LocalDate latestEvaluatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_record_id", referencedColumnName = "id")
    private SurveyRecord surveyRecord;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "program_participant_id", referencedColumnName = "id")
    private ProgramParticipants programParticipants;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;
}
