package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.RecordStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SessionFlow;
import com.fpt.gsu25se47.schoolpsychology.model.enums.StudentCoopLevel;
import lombok.Data;

import java.util.List;

@Data
public class UpdateAppointmentRecordRequest {

    private SessionFlow sessionFlow;

    private StudentCoopLevel studentCoopLevel;

    private RecordStatus status;

    private String noteSummary;

    private String noteSuggest;

    private String reason;

    private Integer totalScore;

    private List<ReportCategoryRequest> reportCategoryRequests;
}
