package com.fpt.gsu25se47.schoolpsychology.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateEnrollmentRequest {

    private Integer classId;
    private List<Integer> studentIds;
}
