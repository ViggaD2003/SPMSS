package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MentalEvaluationStatic {

    private SurveyStatic survey;

    private AppointmentStatic appointment;

}
