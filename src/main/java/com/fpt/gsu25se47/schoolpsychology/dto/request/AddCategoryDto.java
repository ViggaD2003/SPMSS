package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AddCategoryDto {
    private String name;
    private String code;
    private List<Integer> listSubTypeIds;
}
