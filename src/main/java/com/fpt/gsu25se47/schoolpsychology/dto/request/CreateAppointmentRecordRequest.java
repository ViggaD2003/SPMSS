package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RecordStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.List;

@Data
public class CreateAppointmentRecordRequest {
    private Integer appointmentId;

    private SessionFlow sessionFlow;

    private StudentCoopLevel studentCoopLevel;

    private RecordStatus status;

    @JsonIgnore
    private AppointmentStatus appointmentStatus;

    private String noteSummary;

    private String noteSuggest;

    private String reason;

    private Integer totalScore;

//    private List<SubmitAnswerRecordRequest> answerRecordRequests;

    private List<ReportCategoryRequest> reportCategoryRequests;
}

