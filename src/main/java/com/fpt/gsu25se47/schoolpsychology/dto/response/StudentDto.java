package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto extends AccountDto{

    private String studentCode;

    private Boolean isEnableSurvey;

    private ClassDto classDto;
}
