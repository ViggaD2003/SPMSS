package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SourceType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMentalEvaluationRequest {

    private Source source;

    private SourceType sourceType;

    private Float weightedScore;

    private LocalDate firstEvaluatedAt;

    private LocalDate lastEvaluatedAt;

    private Integer studentId;

    private Integer appointmentId;

    private Integer surveyRecordId;

}