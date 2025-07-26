package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SourceType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MentalEvaluationResponse {

    private Integer id;

    private Source source;

    private SourceType sourceType;

    private Float weightedScore;

    private LocalDate firstEvaluatedAt;

    private LocalDate lastEvaluatedAt;

    private Integer appointmentId;

    private Integer surveyRecordId;

    private Integer studentId;

    private Integer caseId;
}
