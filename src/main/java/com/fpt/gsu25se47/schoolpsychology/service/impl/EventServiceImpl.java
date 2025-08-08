package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.EventResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.ProgramStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Source;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Status;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.AppointmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SupportProgramRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EventService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final SurveyRepository surveyRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<EventResponse> getEvents(Integer studentId, LocalDate startDate, LocalDate endDate) {

        Account curAcc = accountRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found for ID: " + studentId));

        List<Appointment> appointments = curAcc.getAppointmentsForMe().stream()
                .filter(a -> !a.getStartDateTime().isBefore(startDate.atStartOfDay())    // a.startDateTime >= start of startDate
                        && !a.getEndDateTime().isAfter(endDate.plusDays(1).atStartOfDay())
                        && a.getStatus() != AppointmentStatus.ABSENT && a.getStatus() != AppointmentStatus.CANCELED && a.getStatus() != AppointmentStatus.COMPLETED)
                .toList();

        List<SupportProgram> supportPrograms = curAcc.getProgramRegistrations()
                .stream()
                .map(ProgramParticipants::getProgram)
                .filter(sp -> !sp.getStartTime().isBefore(startDate.atStartOfDay())
                        && !sp.getEndTime().isAfter(endDate.plusDays(1).atStartOfDay())
                        && sp.getStatus() != ProgramStatus.COMPLETED)
                .toList();

        List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(curAcc.getId());

        List<EventResponse> events = new ArrayList<>();

        events.addAll(appointments.stream()
                .map((appointment) -> new EventResponse(
                        appointment.getId(),
                        appointment.getReasonBooking(),
                        Source.APPOINTMENT,
                        (appointment.getCases() != null && appointment.getCases().getStatus() != Status.CLOSED),
                        appointment.getStartDateTime().toLocalDate(),
                        appointment.getStartDateTime().toLocalTime(),
                        appointment.getStatus().name(),
                        appointment.getLocation()
                ))
                .toList());

        events.addAll(supportPrograms.stream()
                .map(program -> new EventResponse(
                        program.getId(),
                        program.getName(),
                        Source.PROGRAM,
                        program.getProgramRegistrations().stream()
                                .filter(pr -> pr.getStudent().getId().equals(studentId)) // only current student
                                .anyMatch(pr -> pr.getCases() != null && pr.getCases().getStatus() != Status.CLOSED),
                        program.getStartTime().toLocalDate(),
                        program.getStartTime().toLocalTime(),
                        program.getStatus().name(),
                        program.getLocation()
                ))
                .toList());

        events.addAll(surveys.stream()
                .map(survey -> new EventResponse(
                        survey.getId(),
                        survey.getTitle(),
                        Source.SURVEY,
                        survey.getSurveyCaseLinks().stream()
                                .anyMatch(s -> s.getCases() != null && s.getCases().getStatus() != Status.CLOSED),
                        survey.getStartDate(),
                        survey.getStartDate().atStartOfDay().toLocalTime(),
                        survey.getStatus().name(),
                        null
                ))
                .toList());

        return events;
    }
}
