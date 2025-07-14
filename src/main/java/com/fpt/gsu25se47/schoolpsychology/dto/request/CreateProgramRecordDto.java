package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateProgramRecordDto {

    private String description;

    private String summary;

    private Status status;

    private Integer totalScore;

    private Integer programSurveyId;

    private Integer ProgramRegistrationId;

    private List<SubmitAnswerRecordRequest> answersRecordRequests;

}
