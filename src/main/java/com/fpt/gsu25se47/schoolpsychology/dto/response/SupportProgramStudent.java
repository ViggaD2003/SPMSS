package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SupportProgramStudent {

    private Integer participantId;

    private Integer studentId;

    private Integer caseId;

    private LocalDateTime joinAt;

    private String status;

    private Float finalScore;

    private List<SurveyRecordGetAllResponse> surveyRecord;
}
