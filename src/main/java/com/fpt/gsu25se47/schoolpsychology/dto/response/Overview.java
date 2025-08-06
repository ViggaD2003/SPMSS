package com.fpt.gsu25se47.schoolpsychology.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Overview {
    private int totalStudents;

    private int totalPrograms;

    private int totalSurveys;

    private int totalAppointments;

    private int activeCases;
}
