package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddNewAppointment;
import com.fpt.gsu25se47.schoolpsychology.dto.request.ConfirmAppointment;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.HostType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AccountRepository accountRepository;

    private final SlotRepository slotRepository;

    private final TeacherRepository teacherRepository;

    private final CounselorRepository counselorRepository;

    @Override
    public Optional<?> createAppointment(AddNewAppointment request) {
        try {
            Appointment appointment = this.mapToEntity(request);
            AppointmentResponse response = mapToResponse(appointmentRepository.save(appointment));
            return Optional.of(response);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create appointment. Please check your data.");
        }
    }

    @Override
    public Optional<?> showHistoryAppointment() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            List<Appointment> appointments = appointmentRepository.findByBookedBy(account.getId()).stream().filter(data ->
                data.getStatus().name().equals("PENDING") || data.getStatus().name().equals("CONFIRMED")
            ).toList();

            List<AppointmentResponse> responses = appointments.stream().map(this::mapToResponse).toList();

            return Optional.of(responses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create appointment. Please check your data.");
        }
    }

    @Override
    public Optional<?> showAllAppointmentsOfSlots() {
        try{
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Account not found"));


            List<Appointment> appointments = appointmentRepository.findAllBySlotHostedBy(account.getId());

            List<AppointmentResponse> responses = appointments.stream().map(this::mapToResponse).toList();
            return Optional.of(responses);
        } catch (Exception e) {
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not create appointment. Please check your data.");
        }
    }

    @Override
    public Optional<?> updateAppointmentStatus(ConfirmAppointment request) {
        try{
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            appointment.setLocation(request.getLocation());
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
            return Optional.of("Updated status appointment successfully");
        } catch (Exception e){
            log.error("Failed to create survey: {}", e.getMessage(), e);
            throw new RuntimeException("Could not update appointment. Please check your data.");
        }
    }

    @Override
    public Optional<?> cancelAppointment(Integer appointmentId, String reasonCancel) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELED);

        appointmentRepository.save(appointment);

        return Optional.of("Canceled appointment successfully");
    }

    private Appointment mapToEntity(AddNewAppointment request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account bookedBy = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Account bookedFor = null;
        if (request.getBookedForId() != null) {
            bookedFor = accountRepository.findById(request.getBookedForId())
                    .orElseThrow(() -> new RuntimeException("BookedFor not found"));
        }

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        Role role = slot.getHostedBy().getRole();
        HostType hostType = role == Role.TEACHER ? HostType.TEACHER : HostType.COUNSELOR;

        String linkMeet = null;
        if(request.getIsOnline()){
            if (hostType == HostType.TEACHER) {
                Teacher teacher = teacherRepository.findById(slot.getHostedBy().getId())
                        .orElseThrow(() -> new RuntimeException("Teacher not found"));
                linkMeet = teacher.getLinkMeet();
            } else {
                Counselor counselor = counselorRepository.findById(slot.getHostedBy().getId())
                        .orElseThrow(() -> new RuntimeException("Counselor not found"));
                linkMeet = counselor.getLinkMeet();
            }
        }

        String location = linkMeet;

        if(request.getStartDateTime().isBefore(slot.getStartDateTime()) || request.getEndDateTime().isAfter(slot.getEndDateTime())){
            throw new RuntimeException("Start date and end date must be before end date time");
        }

        if(slot.getStatus().name().equals("CLOSED")){
            throw new RuntimeException("Slot is CLOSED");
        }

        return Appointment.builder()
                .slot(slot)
                .bookedBy(bookedBy)
                .bookedFor(bookedFor)
                .isOnline(request.getIsOnline())
                .reason(request.getReason())
                .status(request.getIsOnline() ? AppointmentStatus.CONFIRMED : AppointmentStatus.PENDING)
                .hostType(hostType)
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .location(location)
                .build();
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .location(appointment.getLocation())
                .bookByName(appointment.getBookedBy().getFullName())
                .bookForName(appointment.getBookedFor() == null ? null : appointment.getBookedFor().getFullName())
                .isOnline(appointment.getIsOnline())
                .hostName(appointment.getSlot().getHostedBy().getFullName())
                .endDateTime(appointment.getEndDateTime())
                .startDateTime(appointment.getStartDateTime())
                .reason(appointment.getReason())
                .status(appointment.getStatus())
                .hostType(appointment.getHostType())
                .build();
    }
}
