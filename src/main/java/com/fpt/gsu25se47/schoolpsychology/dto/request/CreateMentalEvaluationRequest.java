package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMentalEvaluationRequest {

    private Source source;

    private Float weightedScore;

    private LocalDate latestEvaluatedAt;

    private Integer studentId;

    private Integer appointmentId;

    private Integer surveyRecordId;

}