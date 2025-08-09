package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseByCategory {
    private String category;
    private int totalCases;
    private int inProgress;
    private int closed;

    public CaseByCategory(String category, int totalCases, int inProgress, int closed) {
        this.category = category;
        this.totalCases = totalCases;
        this.inProgress = inProgress;
        this.closed = closed;
    }
}

