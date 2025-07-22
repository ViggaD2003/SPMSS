package com.fpt.gsu25se47.schoolpsychology.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "survey_case_link")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyCaseLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean isActive;

    private LocalDate removeAt;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "case_id")
    private Cases cases;

    @ManyToOne
    @JoinColumn(name = "assignedBy")
    private Account assignedBy;
}
