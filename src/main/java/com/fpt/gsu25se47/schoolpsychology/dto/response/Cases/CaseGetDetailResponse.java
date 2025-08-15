package com.fpt.gsu25se47.schoolpsychology.dto.response.Cases;

import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationStatic;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CaseGetDetailResponse {

    private CaseGetAllResponse caseInfo;

    private MentalEvaluationStatic groupedStatic;
}
