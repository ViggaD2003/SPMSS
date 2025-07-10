package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportProgramResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Integer maxParticipants;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isOnline;

    private ProgramStatus status;

    private String location;

    private CategoryResponse category;

    private List<Integer> sessions;

    private List<Integer> programRegistrations;

    private List<Integer> programSurveys;
}
