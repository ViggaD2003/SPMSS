package com.fpt.gsu25se47.schoolpsychology.dto.response.Student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto extends AccountDto {

    private String studentCode;

    private Boolean isEnableSurvey;

    private Grade targetLevel;

    private Integer teacherId;

    private Integer caseId;
}
