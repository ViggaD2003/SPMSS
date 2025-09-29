package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.response.EventResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.*;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.CaseRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.RelationshipRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final SurveyRepository surveyRepository;
    private final AccountRepository accountRepository;
    private final CaseRepository caseRepository;
    private final AccountService accountService;
    private final RelationshipRepository relationshipRepository;

    @Override
    public List<EventResponse> getEvents(Integer studentId, LocalDate startDate, LocalDate endDate) {

        Account account = accountService.getCurrentAccount();

        Account curAcc = accountRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found for ID: " + studentId));

        Cases activeCase = caseRepository.findActiveCaseByStudentId(studentId);

        List<Appointment> appointments = curAcc.getAppointmentsForMe().stream()
                .filter(a -> a.getStartDateTime().isBefore(endDate.plusDays(1).atStartOfDay()

                ) // starts before filter end
                        && a.getEndDateTime().isAfter(startDate.atStartOfDay())               // ends after filter start
                        && a.getStatus() != AppointmentStatus.ABSENT
                        && a.getStatus() != AppointmentStatus.CANCELED
                        && a.getStatus() != AppointmentStatus.COMPLETED)
                .toList();

        List<SupportProgram> supportPrograms = curAcc.getProgramRegistrations()
                .stream()
                .map(ProgramParticipants::getProgram)
                .filter(sp -> sp.getStartTime().isBefore(endDate.plusDays(1).atStartOfDay())
                        && sp.getEndTime().isAfter(startDate.atStartOfDay())
                        && sp.getStatus() != ProgramStatus.COMPLETED)
                .toList();

        List<Survey> surveys = surveyRepository.findUnansweredExpiredSurveysByAccountId(curAcc.getId())
                .stream()
                .filter(s -> s.getStartDate().isBefore(endDate.plusDays(1))
                        && s.getEndDate().isAfter(startDate))
                .toList();

        if(account.getRole() == Role.PARENTS && relationshipRepository.checkRelationshipExists(account.getId(), List.of(curAcc.getId()))
                && !activeCase.getNotify()
        ) {
            appointments = appointments.stream().filter(item -> !activeCase.getAppointments().contains(item)).toList();

            List<SupportProgram> programCases = activeCase.getProgramParticipants().stream().map(ProgramParticipants::getProgram).toList();

            supportPrograms = supportPrograms.stream().filter(item -> !programCases.contains(item)).toList();

            List<Survey> surveyCases = activeCase.getSurveyCaseLinks().stream().map(SurveyCaseLink::getSurvey).toList();

            surveys = surveys.stream().filter(item -> !surveyCases.contains(item)).toList();
        }


        List<EventResponse> events = new ArrayList<>();

        events.addAll(appointments.stream()
                .map((appointment) -> new EventResponse(
                        appointment.getId(),
                        "Cuộc hẹn với " + Optional.ofNullable(appointment.getHostType())
                                .map(ht -> ht == HostType.TEACHER ? "giáo viên" : "tư vấn viên")
                                .orElse("không xác định"),
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
                        startDate,
                        startDate.atStartOfDay().toLocalTime(),
                        survey.getStatus().name(),
                        null
                ))
                .toList());

        return events;
    }
}
