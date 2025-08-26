package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assessment_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentScores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "appoinment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Float severityScore;

    private Float frequencyScore;

    private Float impairmentScore;

    private Float chronicityScore;

    private Float compositeScore;
}
