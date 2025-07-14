package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.common.Auditable;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "program_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramRecord extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private String summary;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer totalScore;

    private LocalDate completeAt;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private ProgramSurvey programSurvey;

    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private ProgramRegistration programRegistration;

    @OneToMany(mappedBy = "programRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerRecord> answerRecords = new ArrayList<>();
}
