package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ClassRequest {

    private String teacherCode;

    private String codeClass;

    private LocalDate classYear;

    private List<String> studentCodes;
}
