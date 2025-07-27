package com.fpt.gsu25se47.schoolpsychology.dto.request;

import jakarta.validation.constraints.Future;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;

@Data
public class SupportProgramRequest {

    private String name;

    @Length(min = 1, max = 5000)
    private String description;

    @Range(min = 1, max = 200)
    private Integer maxParticipants;

    @Future
    private LocalDateTime startDate;

    @Future
    private LocalDateTime endDate;

    private String linkMeet;

    private String thumbnail;

    private Integer hostedBy;

    private AddNewSurveyDto addNewSurveyDto;

}
