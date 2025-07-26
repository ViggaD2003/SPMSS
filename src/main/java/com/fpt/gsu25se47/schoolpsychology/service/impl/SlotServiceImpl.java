package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SlotMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//
//import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.response.BookedSlot;
//import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotConflictError;
//import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
//import com.fpt.gsu25se47.schoolpsychology.model.Account;
//import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
//import com.fpt.gsu25se47.schoolpsychology.model.Slot;
//import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
//import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
//import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotUsageType;
//import com.fpt.gsu25se47.schoolpsychology.repository.*;
//import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
@Service
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;
    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final GuardianRepository guardianRepository;
    private final TeacherRepository teacherRepository;
    private final CounselorRepository counselorRepository;

    private final SlotMapper slotMapper;

    @Override
    public List<SlotResponse> getAllSlotsByHostBy(Integer hostById) {

        Account hostBy = accountRepository.findById(hostById)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Host By Account not found for ID: " + hostById));

        List<Slot> publishedSlots = findPublishedSlotsByHost(hostBy.getId());

        return publishedSlots.stream()
                .map(slotMapper::toSlotResponse)
                .toList();

    }

    //
//    @Override
//    public ResponseEntity<?> initSlot(List<AddSlotRequest> requests) {
//        List<Slot> slotsToCreate = new ArrayList<>();
//        List<SlotConflictError> conflictErrors = new ArrayList<>();
//
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        Account account = accountRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("Unauthorized"));
//
//        for (AddSlotRequest req : requests) {
//            if (req.getSlotType().equals(SlotUsageType.APPOINTMENT.name())) {
//                LocalTime officeStart = LocalTime.of(8, 0);
//                LocalTime officeEnd = LocalTime.of(18, 0);
//
//                LocalTime slotStart = req.getStartDateTime().toLocalTime();
//                LocalTime slotEnd = req.getEndDateTime().toLocalTime();
//
//                if (slotStart.isBefore(officeStart) || slotEnd.isAfter(officeEnd)) {
//                    throw new RuntimeException("Slot must be within office hours (08:00 - 18:00)");
//                }
//            } else if (req.getSlotType().equals(SlotUsageType.PROGRAM.name())) {
//                LocalTime officeStart = LocalTime.of(18, 0);
//                LocalTime officeEnd = LocalTime.of(21, 30);
//
//                LocalTime slotStart = req.getStartDateTime().toLocalTime();
//                LocalTime slotEnd = req.getEndDateTime().toLocalTime();
//
//                if (slotStart.isBefore(officeStart) || slotEnd.isAfter(officeEnd)) {
//                    throw new RuntimeException("Slot must be within office hours (18:00 - 21:30)");
//                }
//            }
//
//            List<Slot> conflicts = slotRepository.findConflictingSlots(
//                    account.getId(),
//                    req.getStartDateTime(),
//                    req.getEndDateTime()
//            );
//
//            if (!conflicts.isEmpty()) {
//                conflictErrors.add(SlotConflictError.builder()
//                        .slotName(req.getSlotName())
//                        .startDateTime(req.getStartDateTime())
//                        .endDateTime(req.getEndDateTime())
//                        .reason("Overlaps with existing slot ID(s): " +
//                                conflicts.stream().map(Slot::getId).map(String::valueOf).collect(Collectors.joining(", ")))
//                        .build());
//            } else {
//                Slot slot = mapToEntity(req, account);
//                slotsToCreate.add(slot);
//            }
//        }
//
//        if (!conflictErrors.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "message", "Một số slot bị trùng thời gian",
//                    "conflicts", conflictErrors
//            ));
//        }
//
//
//        slotRepository.saveAll(slotsToCreate);
//        return ResponseEntity.ok("Khởi tạo slot thành công!");
//    }
//
//    @Override
//    public Optional<?> updateSlot(Integer slotId, UpdateSlotRequest request) {
//        try {
//            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));
//
//            if (slot.getAppointments() == null || slot.getProgramSessions() == null) {
//                throw new IllegalArgumentException("Can't not update cause already have appointment or program sessions of this slot !");
//            }
//
//            slot.setSlotName(request.getSlotName());
//            slot.setStartDateTime(request.getStartDateTime());
//            slot.setEndDateTime(request.getEndDateTime());
//
//            Slot savedSlot = slotRepository.save(slot);
//
//            return Optional.of(this.mapToResponse(savedSlot));
//        } catch (Exception e) {
//            log.error("Failed to update slot: {}", e.getMessage(), e);
//            throw new RuntimeException("Something went wrong");
//        }
//    }
//
//    @Override
//    public Optional<?> updateStatusSlot(Integer slotId) {
//        try {
//            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));
//
//            slot.setStatus(SlotStatus.PUBLISHED);
//
//            slotRepository.save(slot);
//            return Optional.of("Update status slot successfully !");
//        } catch (Exception e) {
//            log.error("Failed to update slot status: {}", e.getMessage(), e);
//            throw new RuntimeException("Something went wrong");
//        }
//    }
//
//    @Override
//    public Optional<List<SlotResponse>> getAllSlotsByHostBy(Integer hostById) {
//        Account account = getCurrentAccount();
//        Role role = account.getRole();
//        List<Slot> slots;
//
//        // Validate HOST ID for STUDENT or PARENTS
//        if ((role == Role.STUDENT || role == Role.PARENTS) && hostById == null) {
//            throw new IllegalArgumentException("Host By id is null");
//        }
//
//        switch (role) {
//            case STUDENT -> {
//                validateStudentExists(account.getId());
//                slots = findPublishedSlotsByHost(hostById);
//            }
//            case PARENTS -> {
//                validateGuardianExists(account.getId());
//                slots = findPublishedSlotsByHost(hostById);
//            }
//            case COUNSELOR -> {
//                validateCounselorExists(account.getId());
//                slots = slotRepository.findAllByHostedById(account.getId());
//            }
//            case TEACHER -> {
//                validateTeacherExists(account.getId());
//                slots = slotRepository.findAllByHostedById(account.getId());
//            }
//            default -> slots = slotRepository.findAll();
//        }
//
//        List<SlotResponse> responses = slots.stream()
//                .map(this::mapToResponse)
//                .toList();
//
//        return Optional.of(responses);
//    }
//
//
//    @Override
//    public Optional<?> getSlotById(Integer slotId) {
//        try {
//            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));
//            SlotResponse response = mapToResponse(slot);
//
//            return Optional.of(response);
//        } catch (Exception e) {
//            log.error("Failed to view slot: {}", e.getMessage(), e);
//            throw new RuntimeException("Something went wrong");
//        }
//    }
//
    @Override
    public Slot createSlot(CreateSlotRequest request) {

        if (request.getStartDateTime().isEqual(request.getEndDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not allow Start Date and End Date are equals");
        }

        Account account = accountRepository.findById(request.getHostById())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found by ID: " + request.getHostById()
                ));

        Slot existingSlot = slotRepository.findExactSlotByStartAndEndMinute(
                request.getHostById(),
                request.getStartDateTime(),
                request.getEndDateTime()
        );

        if (existingSlot != null) {
            boolean isSlotReusable = existingSlot.getAppointments().stream()
                    .allMatch(s -> s.getStatus() == AppointmentStatus.COMPLETED
                            || s.getStatus() == AppointmentStatus.CANCELED
                            || s.getStatus() == AppointmentStatus.ABSENT);

            if (existingSlot.getStatus() != SlotStatus.PUBLISHED || isSlotReusable) {
                return existingSlot;
            }
        }

        Slot slotCreated = slotMapper.toSlot(request, account);
        return slotRepository.save(slotCreated);
    }

    @Override
    public SlotResponse updateStatusSlot(Integer slotId, SlotStatus status) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Slot not found for ID: " + slotId));

        slot.setStatus(status);

        return slotMapper.toSlotResponse(slot);
    }
//
//
//    private Slot mapToEntity(AddSlotRequest request, Account account) {
//        if (request.getStartDateTime().isAfter(request.getEndDateTime())) {
//            throw new IllegalArgumentException("Start time must be before end time");
//        }
//
//        SlotUsageType usageType;
//        try {
//            usageType = SlotUsageType.valueOf(request.getSlotType().toUpperCase());
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException("Invalid slot type: " + request.getSlotType());
//        }
//
//        return Slot.builder()
//                .startDateTime(request.getStartDateTime())
//                .endDateTime(request.getEndDateTime())
//                .status(SlotStatus.DRAFT)
//                .hostedBy(account)
//                .slotName(request.getSlotName())
//                .type(usageType)
//                .build();
//    }
//
//    private SlotResponse mapToResponse(Slot slot) {
//        List<Appointment> appointments = appointmentRepository.findAllBySlotId(slot.getId());
//        List<BookedSlot> bookedSlots = appointments.stream().map(this::mapToBookedSlot).toList();
//        return SlotResponse.builder()
//                .slotName(slot.getSlotName())
//                .id(slot.getId())
//                .status(slot.getStatus().name())
//                .startDateTime(slot.getStartDateTime())
//                .endDateTime(slot.getEndDateTime())
//                .roleName(slot.getHostedBy().getRole().name())
//                .fullName(slot.getHostedBy().getFullName())
//                .slotType(slot.getType().name())
//                .createdAt(slot.getCreatedDate())
//                .updatedAt(slot.getUpdatedDate())
//                .booked(bookedSlots)
//                .build();
//    }
//
//    private BookedSlot mapToBookedSlot(Appointment appointment) {
//        return BookedSlot.builder()
//                .startDateTime(appointment.getStartDateTime())
//                .endDateTime(appointment.getEndDateTime())
//                .build();
//    }
//
//
//    private Account getCurrentAccount() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return accountRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
//    }
//
//    private void validateStudentExists(Integer id) {
//        studentRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
//    }
//
//    private void validateGuardianExists(Integer id) {
//        guardianRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Guardian not found"));
//    }
//
//    private void validateTeacherExists(Integer id) {
//        teacherRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
//    }
//
//    private void validateCounselorExists(Integer id) {
//        counselorRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Counselor not found"));
//    }

    private List<Slot> findPublishedSlotsByHost(Integer hostById) {
        return slotRepository.findAllByHostedById(hostById).stream()
                .filter(slot -> slot.getStatus() == SlotStatus.PUBLISHED)
                .toList();
    }
//
}
