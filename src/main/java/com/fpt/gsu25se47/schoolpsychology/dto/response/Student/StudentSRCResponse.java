package com.fpt.gsu25se47.schoolpsychology.dto.response.Student;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentSRCResponse extends AccountDto {

    private String studentCode;

    private Boolean isEnableSurvey;

    private Integer caseId;

    private SurveyRecordGetAllResponse latestSurveyRecord;
}
