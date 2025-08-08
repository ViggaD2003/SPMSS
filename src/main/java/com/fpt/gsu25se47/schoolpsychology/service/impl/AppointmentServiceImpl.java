package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateAppointmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.AppointmentMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final TeacherRepository teacherRepository;
    private final CounselorRepository counselorRepository;
    private final SlotRepository slotRepository;
    private final CaseRepository caseRepository;
    private final AccountService accountService;
    private final AssessmentScoresService assessmentScoresService;
    private final MentalEvaluationService mentalEvaluationService;
    private final SystemConfigService systemConfigService;
    private final AppointmentMapper appointmentMapper;

    @Transactional
    @Override
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {

        // system config for appointment feature enabled
        validateAppointmentFeatureEnabled();

        // bookedBy ( person who book appointment )
        Account bookedBy = accountService.getCurrentAccount();

        // system config for max appointments per day
        validateMaxAppointments(request, bookedBy);

//        ensureNoAppointmentConflicts(request);

        Account bookedFor = getBookedFor(request);

        Slot slot = getSlot(request);

        validateTimeWithinSlot(request, slot);

        String location = determineLocation(request, slot);

        Appointment appointment = appointmentMapper.toAppointment(request);

        appointment.setBookedBy(bookedBy);
        appointment.setBookedFor(bookedFor);
        appointment.setSlot(slot);
        appointment.setLocation(location);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        AppointmentResponse appointmentResponse = appointmentMapper.toAppointmentResponse(
                appointmentRepository.save(appointment)
        );

        appointmentResponse.setSystemConfigs(systemConfigService.getConfigsByGroup("APPOINTMENT"));

        return appointmentResponse;
    }

    @Override
    public AppointmentResponse cancelAppointment(Integer appointmentId, String reasonCancel) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found for ID: " + appointmentId
                ));

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointment.getSlot().setStatus(SlotStatus.CLOSED);
        appointment.setCancelReason(reasonCancel);

        return appointmentMapper.toAppointmentResponse(
                appointmentRepository.save(appointment));
    }

    @Transactional
    @Override
    public AppointmentResponse updateAppointment(Integer appointmentId, UpdateAppointmentRequest request) {

        Account account = accountService.getCurrentAccount();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found for ID: " + appointmentId
                ));

        Cases cases = null;
        if (request.getCaseId() != null) {
            cases = caseRepository.findById(request.getCaseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Case not found for ID: " + request.getCaseId()));
        }
        MentalEvaluation mentalEvaluationSaved = null;
        if (account.getRole() == Role.COUNSELOR) {

            request.getAssessmentScores().forEach(t -> assessmentScoresService.createAssessmentScoresWithContext(t, appointment));
            mentalEvaluationSaved = mentalEvaluationService.createMentalEvaluationWithContext(
                    appointment,
                    null,
                    null
            );
        }


        Appointment appointmentUpdated = appointmentMapper.updateAppointmentFromRequest(request, appointment);

        appointmentUpdated.setMentalEvaluation(mentalEvaluationSaved);
        appointmentUpdated.setCases(cases);
        appointmentUpdated.setStatus(AppointmentStatus.COMPLETED);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointmentUpdated));
    }

    @Transactional
    @Override
    public AppointmentResponse updateStatus(Integer appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found for ID: " + appointmentId
                ));

        if (status == AppointmentStatus.CANCELED || status == AppointmentStatus.ABSENT || status == AppointmentStatus.COMPLETED) {
            appointment.getSlot().setStatus(SlotStatus.PUBLISHED);
        } else {
            appointment.getSlot().setStatus(SlotStatus.CLOSED);
        }

        appointment.setStatus(status);

        return appointmentMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse getAppointmentById(Integer appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Appointment not found for ID: " + appointmentId));

        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByStatus(AppointmentStatus appointmentStatus) {

        List<Appointment> appointments = appointmentRepository.findAllByStatus(appointmentStatus);
        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponseWithoutAS)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAllAccAppointmentsByStatuses(Integer accountId, List<AppointmentStatus> statuses) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found for ID: " + accountId));

        List<Appointment> appointments = getAppointmentsByRoleAndStatus(statuses, account);

        return appointments.stream()
                .map(appointmentMapper::toAppointmentResponseWithoutAS)
                .toList();
    }

    private List<Appointment> getAppointmentsByRoleAndStatus(List<AppointmentStatus> statuses, Account account) {

        List<Appointment> appointments = new ArrayList<>();
        if (account.getRole() == Role.PARENTS) {

            appointments = appointmentRepository.findByBookedByAndStatus(account.getId(), statuses);
        } else if (account.getRole() == Role.STUDENT) {

            appointments = appointmentRepository.findByBookedForAndStatus(account.getId(), statuses);
        } else if (account.getRole() == Role.COUNSELOR) {

            appointments = appointmentRepository.findAllByHostByWithStatus(account.getId(), statuses);
        }
        return appointments;
    }

    private String determineLocation(CreateAppointmentRequest request, Slot slot) {
        // location for online and offline appointments
        Role role = slot.getHostedBy().getRole();
        HostType hostType = role == Role.TEACHER ? HostType.TEACHER : HostType.COUNSELOR;

        String location;
        if (request.getIsOnline()) {
            if (hostType == HostType.TEACHER) {
                Teacher teacher = teacherRepository.findById(slot.getHostedBy().getId())
                        .orElseThrow(() -> new RuntimeException("Teacher not found"));
                location = teacher.getLinkMeet();
            } else {
                Counselor counselor = counselorRepository.findById(slot.getHostedBy().getId())
                        .orElseThrow(() -> new RuntimeException("Counselor not found"));
                location = counselor.getLinkMeet();
            }
        } else {
            location = "Phòng chăm sóc tinh thần";
        }
        return location;
    }

    private void validateTimeWithinSlot(CreateAppointmentRequest request, Slot slot) {
        if (request.getStartDateTime().isEqual(request.getEndDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Start and end time must not be equal");
        }

        // check if the time from appointment is in the slot time range
        if (request.getStartDateTime().isBefore(slot.getStartDateTime()) ||
                request.getEndDateTime().isAfter(slot.getEndDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Start and end time must be within the slot time range");
        }
    }

    private Slot getSlot(CreateAppointmentRequest request) {

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Slot not found"));

        if (slot.getStatus() == SlotStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slot is CLOSED");
        }

        return slot;
    }

    private Account getBookedFor(CreateAppointmentRequest request) {
        // bookdedFor -> person to host this appointment
        Account bookedFor = null;
        if (request.getBookedForId() != null) {
            bookedFor = accountRepository.findById(request.getBookedForId())
                    .orElseThrow(() -> new RuntimeException("BookedFor not found"));
        }
        return bookedFor;
    }

    private void ensureNoAppointmentConflicts(CreateAppointmentRequest request) {

        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(
                request.getSlotId(),
                request.getStartDateTime(),
                request.getEndDateTime()
        );

        if (!conflictingAppointments.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "There's already appointment booking this time");
        }
    }

    private void validateMaxAppointments(CreateAppointmentRequest request, Account bookedBy) {
        Integer maxAppointments = systemConfigService.getValueAs("appointment.max_per_day", Integer.class);

        LocalDateTime startOfDay = request.getStartDateTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        int currentAppointments = appointmentRepository.countByBookedByIdAndStartDateTimeBetween(
                bookedBy.getId(), startOfDay, endOfDay
        );

        if (currentAppointments >= maxAppointments) {
            throw new IllegalStateException("Your account has reached the maximum appointments for the day.");
        }
    }

    private void validateAppointmentFeatureEnabled() {
        Boolean appointmentEnabled = systemConfigService.getValueAs("appointment.enabled", Boolean.class);
        if (!appointmentEnabled) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Appointment feature is currently disabled.");
        }
    }
}