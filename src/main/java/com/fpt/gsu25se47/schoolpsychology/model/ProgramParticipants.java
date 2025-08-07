package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "program_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private SupportProgram program;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Account student;

    @ManyToOne
    @JoinColumn(name = "case_id", nullable = true)
    private Cases cases;

    @OneToOne(mappedBy = "programParticipants")
    private MentalEvaluation mentalEvaluation;

    private LocalDateTime joinAt;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    private Float finalScore;

}
