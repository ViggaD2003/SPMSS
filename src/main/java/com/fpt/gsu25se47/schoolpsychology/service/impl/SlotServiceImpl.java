package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.BookedSlot;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotConflictError;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotUsageType;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;

    private final AccountRepository accountRepository;

    private final StudentRepository studentRepository;

    @Override
    public ResponseEntity<?> initSlot(List<AddSlotRequest> requests) {
        List<Slot> slotsToCreate = new ArrayList<>();
        List<SlotConflictError> conflictErrors = new ArrayList<>();

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account account = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Unauthorized"));

        for (AddSlotRequest req : requests) {
            List<Slot> conflicts = slotRepository.findConflictingSlots(
                    account.getId(),
                    req.getStartDateTime(),
                    req.getEndDateTime()
            );

            if (!conflicts.isEmpty()) {
                conflictErrors.add(SlotConflictError.builder()
                        .slotName(req.getSlotName())
                        .startDateTime(req.getStartDateTime())
                        .endDateTime(req.getEndDateTime())
                        .reason("Overlaps with existing slot ID(s): " +
                                conflicts.stream().map(Slot::getId).map(String::valueOf).collect(Collectors.joining(", ")))
                        .build());
            } else {
                Slot slot = mapToEntity(req, account);
                slotsToCreate.add(slot);
            }
        }

        if (!conflictErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Một số slot bị trùng thời gian",
                    "conflicts", conflictErrors
            ));
        }

        slotRepository.saveAll(slotsToCreate);
        return ResponseEntity.ok("Khởi tạo slot thành công!");
    }

    @Override
    public Optional<?> updateSlot(Integer slotId, UpdateSlotRequest request) {
        try {
            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));

            if (slot.getAppointments() == null || slot.getProgramSessions() == null) {
                throw new IllegalArgumentException("Can't not update cause already have appointment or program sessions of this slot !");
            }

            slot.setSlotName(request.getSlotName());
            slot.setStartDateTime(request.getStartDateTime());
            slot.setEndDateTime(request.getEndDateTime());

            Slot savedSlot = slotRepository.save(slot);

            return Optional.of(this.mapToResponse(savedSlot));
        } catch (Exception e) {
            log.error("Failed to update slot: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> updateStatusSlot(Integer slotId) {
        try {
            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));

            slot.setStatus(SlotStatus.PUBLISHED);

            slotRepository.save(slot);
            return Optional.of("Update status slot successfully !");
        } catch (Exception e) {
            log.error("Failed to update slot status: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<List<SlotResponse>> getAllSlotsByHostBy(Integer hostById) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));

            List<Slot> slots;

            if (account.getRole().name().equals("STUDENT")) {
                Student student = studentRepository.findById(account.getId()).orElseThrow(() -> new IllegalArgumentException("Student not found"));

                if (hostById == null) {
                    if (student.getClasses() != null) {
                        hostById = student.getClasses().getTeacher().getId();
                    } else {
                        throw new IllegalArgumentException("Student not in any class so dont have teacher to book slot !");
                    }
                }

                slots = slotRepository.findAllByHostedById(hostById).stream()
                        .filter(slot -> slot.getStatus().name().equals("PUBLISHED"))
                        .toList();
            } else {
                slots = (hostById != null)
                        ? slotRepository.findAllByHostedById(hostById)
                        : slotRepository.findAll();
            }

            List<SlotResponse> responses = slots.stream()
                    .map(this::mapToResponse)
                    .toList();

            return Optional.of(responses);
    }

    @Override
    public Optional<?> getSlotById(Integer slotId) {
        try {
            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));
            SlotResponse response = mapToResponse(slot);

            return Optional.of(response);
        } catch (Exception e) {
            log.error("Failed to view slot: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }


    private Slot mapToEntity(AddSlotRequest request, Account account) {
        if (request.getStartDateTime().isAfter(request.getEndDateTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        SlotUsageType usageType;
        try {
            usageType = SlotUsageType.valueOf(request.getSlotType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid slot type: " + request.getSlotType());
        }

        return Slot.builder()
                .startDateTime(request.getStartDateTime())
                .endDateTime(request.getEndDateTime())
                .status(SlotStatus.DRAFT)
                .hostedBy(account)
                .slotName(request.getSlotName())
                .type(usageType)
                .build();
    }

    private SlotResponse mapToResponse(Slot slot) {
        List<Appointment> appointments = slot.getAppointments();
        List<BookedSlot> bookedSlots = appointments.stream().map(this::mapToBookedSlot).toList();
        return SlotResponse.builder()
                .slotName(slot.getSlotName())
                .id(slot.getId())
                .status(slot.getStatus().name())
                .startDateTime(slot.getStartDateTime())
                .endDateTime(slot.getEndDateTime())
                .roleName(slot.getHostedBy().getRole().name())
                .fullName(slot.getHostedBy().getFullName())
                .slotType(slot.getType().name())
                .createdAt(slot.getCreatedDate())
                .updatedAt(slot.getUpdatedDate())
                .Booked(bookedSlots)
                .build();
    }

    private BookedSlot mapToBookedSlot(Appointment appointment) {
        return BookedSlot.builder()
                .startDateTime(appointment.getStartDateTime())
                .endDateTime(appointment.getEndDateTime())
                .build();
    }
}
