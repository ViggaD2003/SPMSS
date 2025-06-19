package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "program_surveys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgramSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer questionId;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    private LocalDate createAt;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private SupportProgram program;

    @OneToMany(mappedBy = "programSurvey")
    private List<ProgramRecord> programRecords = new ArrayList<>();

    @OneToMany(mappedBy = "programSurvey")
    private List<Question> questions = new ArrayList<>();
}
