package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateMentalEvaluationRequest {

    private Source source;

    private Float weightedScore;

    private LocalDateTime latestEvaluatedAt;

    private Integer studentId;

    private Integer appointmentId;

    private Integer surveyRecordId;

    private Integer programParticipantId;

}