package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryResponse {

    private int id;

    private String name;

    private String code;

    private List<SubTypeResponse> subTypes;
}
