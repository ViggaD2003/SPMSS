package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.GradeDto;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import org.springframework.stereotype.Component;

@Component
public class GradeMapper {

    public GradeDto from(Grade grade) {
        return GradeDto.builder()
                .targetLevel(grade.name())
                .build();
    }
}
