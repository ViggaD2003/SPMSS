package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Integer id;

    private String name;

    private String code;

    private String description;

    private Boolean isSum;

    private Boolean isLimited;

    private Integer questionLength;

    private Float severityWeight;

    private Boolean isActive;

    private Integer maxScore;

    private Integer minScore;

}
