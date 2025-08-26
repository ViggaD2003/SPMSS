package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "survey_record_id")
    private SurveyRecord surveyRecord;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private boolean isSkipped;
}