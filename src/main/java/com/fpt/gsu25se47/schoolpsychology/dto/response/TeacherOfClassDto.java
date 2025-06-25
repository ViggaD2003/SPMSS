package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherOfClassDto {

    private String teacherCode;

    private String fullName;

    private String phoneNumber;

    private String email;
}
