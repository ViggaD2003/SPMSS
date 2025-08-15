package com.fpt.gsu25se47.schoolpsychology.dto.response.SchoolYear;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SchoolYearResponse {

    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String name;
}
