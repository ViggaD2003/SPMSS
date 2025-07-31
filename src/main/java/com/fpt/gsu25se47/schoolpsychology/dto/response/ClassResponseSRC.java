package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ClassResponseSRC {

    private Integer id;

    private Grade grade;

    private TeacherDto teacher;

    private String codeClass;

    private String schoolYear;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isActive;

    private List<StudentSRCResponse> students;
}
