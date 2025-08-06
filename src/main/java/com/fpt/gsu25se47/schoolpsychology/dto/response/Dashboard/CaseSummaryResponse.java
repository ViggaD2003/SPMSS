package com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseSummaryResponse {

    private Integer categoryId;
    private String categoryName;
    private String categoryCode;
    private Long caseCount;
}
