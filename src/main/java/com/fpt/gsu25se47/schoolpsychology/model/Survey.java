package com.fpt.gsu25se47.schoolpsychology.model;

import com.fpt.gsu25se47.schoolpsychology.model.enums.SurveyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    private Boolean isRequired;

    private Boolean isRecurring;

    private String recurringCycle; // e.g., "WEEKLY", "MONTHLY", etc.

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate createAt;

    private LocalDate updateAt;

    @OneToMany(mappedBy = "survey")
    private List<SurveyRecord> surveyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "survey")
    private List<Question> questions = new ArrayList<>();
}
