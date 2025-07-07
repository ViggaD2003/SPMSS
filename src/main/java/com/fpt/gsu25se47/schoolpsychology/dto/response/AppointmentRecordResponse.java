package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AppointmentRecordResponse {
    private Integer id;
    private AppointmentResponse appointment;

    private String sessionFlow;

    private String studentCoopLevel;

    private String status;

    private String noteSummary;

    private String noteSuggest;

    private String reason;

    private Integer totalScore;

    private List<AnswerRecordResponse> answerRecords;
}
