package com.fpt.gsu25se47.schoolpsychology.service.inter;

import com.fpt.gsu25se47.schoolpsychology.dto.response.EventResponse;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    List<EventResponse> getEvents(Integer studentId, LocalDate startDate, LocalDate endDate);
}
