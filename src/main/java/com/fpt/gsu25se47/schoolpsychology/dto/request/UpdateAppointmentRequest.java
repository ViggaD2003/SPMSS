package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Data
public class UpdateAppointmentRequest {

    private Integer caseId;

    private String sessionNotes;

    private String noteSummary;

    private String noteSuggestion;

    private SessionFlow sessionFlow;

    private StudentCoopLevel studentCoopLevel;

    private List<CreateAssessmentScoreRequest> assessmentScores;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CreateMentalEvaluationRequest mentalEvaluationRequest;
}
