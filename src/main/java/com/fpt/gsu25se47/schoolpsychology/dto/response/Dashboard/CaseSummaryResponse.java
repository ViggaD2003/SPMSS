package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import lombok.Data;

@Data
public class CaseSummaryResponse {

    private String categoryName;
    private String categoryCode;
    private Integer count;
}
