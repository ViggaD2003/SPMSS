package com.fpt.gsu25se47.schoolpsychology.dto.response.Student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto extends AccountDto {

    private String studentCode;

    private Boolean isEnableSurvey;

    private List<MentalEvaluationDto> mentalEvaluations;

    private ClassDto classDto;
}
