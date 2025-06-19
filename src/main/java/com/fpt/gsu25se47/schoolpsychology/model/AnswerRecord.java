package com.fpt.gsu25se47.schoolpsychology.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "program_record_id", referencedColumnName = "id")
    private ProgramRecord programRecord;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_record_id", referencedColumnName = "id")
    private SurveyRecord surveyRecord;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id", referencedColumnName = "id")
    private Answer answer;

    private String otherAnswer;

    private boolean isSkipped;
}
