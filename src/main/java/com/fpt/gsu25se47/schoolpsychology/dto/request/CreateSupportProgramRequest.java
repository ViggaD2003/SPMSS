package com.fpt.gsu25se47.schoolpsychology.dto.request;

import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.validations.StartBeforeEndDateConstraint;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.util.List;

@Data
@StartBeforeEndDateConstraint
public class CreateSupportProgramRequest {

    private String name;

    @Length(min = 0, max = 5000)
    private String description;

    @Range(min = 1, max = 200)
    private Integer maxParticipants;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isOnline;

    private ProgramStatus status;

    private String location;

    private Integer categoryId;

    private List<Integer> sessionIds;
}
