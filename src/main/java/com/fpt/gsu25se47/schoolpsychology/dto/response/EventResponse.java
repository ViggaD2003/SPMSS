package com.fpt.gsu25se47.schoolpsychology.dto.response;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EventResponse {

    private Integer relatedId;
    private String title;
    private Source source;
    private boolean fromCase;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String location;
}
