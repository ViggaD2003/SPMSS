package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.google.api.services.calendar.model.Event;

public interface GoogleCalendarService {
    Event createMeetLinkForTeacher(String teacherName, String roleName);
}
