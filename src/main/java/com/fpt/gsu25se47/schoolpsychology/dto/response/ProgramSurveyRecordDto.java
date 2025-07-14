package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProgramSurveyRecordDto {

    private Integer id;

    private String description;

    private String summary;

    private String status;

    private Integer totalScore;

    private LocalDate completedAt;

    private ProgramSurveyDto programSurveyDto;

    private List<AnswerRecordResponse> answerRecords;
}
