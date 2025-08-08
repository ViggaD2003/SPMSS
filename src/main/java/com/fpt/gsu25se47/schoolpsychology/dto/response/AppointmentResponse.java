package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.model.SystemConfig;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    private Integer id;

//    private SlotResponse slot;

    private AccountDto bookedFor;

    private AccountDto bookedBy;

    private AccountDto hostedBy;

    private Boolean isOnline;

    private String location;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private HostType hostType;

    private AppointmentStatus status;

    private String sessionNotes;

    private String reasonBooking;

    private String noteSummary;

    private String noteSuggestion;

    private String cancelReason;

    private SessionFlow sessionFlow;

    private StudentCoopLevel studentCoopLevel;

    private List<AssessmentScoreResponse> assessmentScores;

    private List<SystemConfig> systemConfigs;
}
