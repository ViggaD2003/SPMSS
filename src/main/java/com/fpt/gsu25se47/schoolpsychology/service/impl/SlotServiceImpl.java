package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotConflictError;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.exception.SlotConflictException;
import com.fpt.gsu25se47.schoolpsychology.mapper.SlotMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final SlotMapper slotMapper;

    @Override
    public SlotResponse updateSlot(Integer slotId, UpdateSlotRequest request) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Slot not found for ID: " + slotId));

        Slot slotUpdated = slotMapper.updateSlotFromRequest(request, slot);

        Slot slotSaved = slotRepository.save(slotUpdated);
        return slotMapper.toSlotResponse(slotSaved, slotSaved.getAppointments());
    }

    @Override
    public List<SlotResponse> getAllSlotsByHostBy(Integer hostById) {

        Account curAccount = accountService.getCurrentAccount();

        Account hostBy = accountRepository.findById(hostById)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Host By Account not found for ID: " + hostById));

        List<Slot> retrievedSlots = getSlotsByRole(hostById, curAccount, hostBy);

        return retrievedSlots.stream()
                .map(s -> slotMapper.toSlotResponse(s, s.getAppointments()))
                .toList();
    }

    private List<Slot> getSlotsByRole(Integer hostById, Account curAccount, Account hostBy) {

        List<Slot> retrievedSlots;
        if (curAccount.getRole() == Role.STUDENT || curAccount.getRole() == Role.PARENTS) {

            retrievedSlots = slotRepository.findAllByHostedByIdAndStatus(hostBy.getId(), SlotStatus.PUBLISHED);
        } else if (curAccount.getRole() == Role.COUNSELOR || curAccount.getRole() == Role.TEACHER) {

            if (Objects.equals(hostById, curAccount.getId())) {
                retrievedSlots = slotRepository.findAllByHostedByIdAndStatus(hostById, SlotStatus.PUBLISHED);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "HostById is not the same ID as current account logged in for role TEACHER, COUNSELOR");
            }
        } else {
            if (Objects.equals(hostById, curAccount.getId())) {
                retrievedSlots = slotRepository.findAll();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "HostById is not the same ID as current account logged in for role MANAGER");
            }
        }
        return retrievedSlots;
    }

    @Override
    @Transactional
    public List<SlotResponse> createSlots(List<CreateSlotRequest> requests) {
        List<Slot> slotsToCreate = new ArrayList<>();
        List<Slot> resultSlots = new ArrayList<>();

        for (CreateSlotRequest request : requests) {
            validateStartAndEndTime(request);

            Account account = getHostAccount(request);

            checkSlotConflicts(requests, account);

            Slot reusableSlot = getReusableSlotIfExists(request);
            if (reusableSlot != null) {
                resultSlots.add(reusableSlot);
                continue;
            }

            validateOfficeHours(request);

            Slot newSlot = slotMapper.toSlot(request, account);
            slotsToCreate.add(newSlot);
        }

        // Save all new slots in one batch
        List<Slot> savedSlots = slotRepository.saveAll(slotsToCreate);
        resultSlots.addAll(savedSlots);

        return resultSlots.stream()
                .map(s -> slotMapper.toSlotResponse(s, s.getAppointments()))
                .toList();
    }

    @Override
    public SlotResponse updateStatusSlot(Integer slotId, SlotStatus status) {

        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Slot not found for ID: " + slotId));

        slot.setStatus(status);

        return slotMapper.toSlotResponseWithoutAppointments(slot);
    }

//    private List<Slot> findPublishedSlotsByHost(Integer hostById) {
//        return slotRepository.findAllByHostedById(hostById).stream()
//                .filter(slot -> slot.getStatus() == SlotStatus.PUBLISHED)
//                .toList();
//    }

    private static void validateOfficeHours(CreateSlotRequest request) {
        LocalTime officeStart = LocalTime.of(8, 0);   // 8:00 AM
        LocalTime officeEnd = LocalTime.of(17, 0);    // 17:00 PM

        LocalTime slotStart = request.getStartDateTime().toLocalTime();
        LocalTime slotEnd = request.getEndDateTime().toLocalTime();

        if (slotStart.isBefore(officeStart) || slotEnd.isAfter(officeEnd)) {
            throw new RuntimeException("Slot must be within office hours (08:00 - 15:00)");
        }
    }

    private Slot getReusableSlotIfExists(CreateSlotRequest request) {

        Slot existingSlot = slotRepository.findExactSlotByStartAndEndMinute(
                request.getHostById(),
                request.getStartDateTime(),
                request.getEndDateTime()
        );

        if (existingSlot != null) {
            boolean isSlotReusable = existingSlot.getAppointments().stream()
                    .allMatch(a -> a.getStatus() == AppointmentStatus.COMPLETED
                            || a.getStatus() == AppointmentStatus.CANCELED
                            || a.getStatus() == AppointmentStatus.ABSENT);

            if (existingSlot.getStatus() != SlotStatus.PUBLISHED || isSlotReusable) {
                return existingSlot;
            }
        }
        return null;
    }

    private void checkSlotConflicts(List<CreateSlotRequest> requests, Account account) {

        Map<CreateSlotRequest, Slot> conflictingMap = new HashMap<>();

        for (CreateSlotRequest request : requests) {
            Slot conflict = slotRepository.findConflictingSlot(
                    account.getId(),
                    request.getStartDateTime(),
                    request.getEndDateTime()
            );
            if (conflict != null) {
                conflictingMap.put(request, conflict);
            }
        }

        if (!conflictingMap.isEmpty()) {
            List<String> allConflictIds = conflictingMap.values().stream()
                    .map(Slot::getId)
                    .map(String::valueOf)
                    .distinct()
                    .collect(Collectors.toList());

            List<SlotConflictError> conflictErrors = conflictingMap.keySet().stream()
                    .map(slot -> SlotConflictError.builder()
                            .startDateTime(slot.getStartDateTime())
                            .endDateTime(slot.getEndDateTime())
                            .reason("Overlaps with existing slot ID(s): " + String.join(", ", allConflictIds))
                            .build())
                    .collect(Collectors.toList());

            throw new SlotConflictException(conflictErrors);
        }
    }


    private Account getHostAccount(CreateSlotRequest request) {
        Account account = accountRepository.findById(request.getHostById())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Account not found by ID: " + request.getHostById()
                ));

        if (account.getRole() != Role.COUNSELOR && account.getRole() != Role.TEACHER && account.getRole() != Role.MANAGER) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This account not allowed to be Host");
        }
        return account;
    }

    private static void validateStartAndEndTime(CreateSlotRequest request) {
        if (request.getStartDateTime().isEqual(request.getEndDateTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Start and End DateTime cannot be equal for host ID: " + request.getHostById());
        }
    }
}
