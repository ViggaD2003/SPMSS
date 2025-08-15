package com.fpt.gsu25se47.schoolpsychology.dto.response.Classes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear.SchoolYearResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Term.TermResponse;
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

    private SchoolYearResponse schoolYear;

    private List<TermResponse> terms;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isActive;

    private List<StudentSRCResponse> students;
}
