package com.fpt.gsu25se47.schoolpsychology.dto.response.Term;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TermResponse {

    private Integer id;

    private Integer termNumber;

    private LocalDate startDate;

    private LocalDate endDate;
}
