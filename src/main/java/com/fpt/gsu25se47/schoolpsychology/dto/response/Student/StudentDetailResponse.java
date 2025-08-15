package com.fpt.gsu25se47.schoolpsychology.dto.response.Student;

import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class StudentDetailResponse extends AccountDto {

    private String studentCode;

    private Boolean isEnableSurvey;

    private List<ParentBaseResponse> parents;

    private List<CaseGetAllForStudentResponse> cases;
}
