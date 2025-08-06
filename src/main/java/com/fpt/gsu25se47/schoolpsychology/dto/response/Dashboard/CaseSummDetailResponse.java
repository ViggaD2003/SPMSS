package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CaseSummDetailResponse {

    private String studentName;
    private String categoryName;
    private Status status;
    private String level;
    private String counselorName;
    private LocalDate lastUpdated;
}
