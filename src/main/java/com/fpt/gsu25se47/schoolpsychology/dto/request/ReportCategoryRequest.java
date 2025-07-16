package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportCategoryRequest {

    private Integer categoryId;
    private BigDecimal score;
}
