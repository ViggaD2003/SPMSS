package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoAccount{
    private Integer id;
    private String fullName;
    private String codeStaff;
}