package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentSRCResponse extends AccountDto{

    private String studentCode;

    private Boolean isEnableSurvey;

    private SurveyRecordGetAllResponse latestSurveyRecord;
}
