package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import lombok.Data;

@Data
public class StudentClaimDto {

    private Integer id;

    private String email;

    private String phoneNumber;

    private String fullName;

    private Boolean gender;

    private String dob;

    private String roleName;

    private String studentCode;

    private Boolean isEnableSurvey;

    private Grade targetLevel;

    private Integer teacherId;

    private Integer caseId;

//    private ClassDto classDto;
}
