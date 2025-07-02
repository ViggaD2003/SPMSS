package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotUsageType;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SlotService;
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
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;

    private final AccountRepository accountRepository;

    @Override
    public Optional<?> initSlot(List<AddSlotRequest> requests) {
        try{
            List<Slot> slots = requests.stream().map(this::mapToEntity).toList();
            slotRepository.saveAll(slots);
            return Optional.of("Init new slot successfully !");
        } catch (Exception e){
            log.error("Failed to create slot: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> updateSlot(Integer slotId, UpdateSlotRequest request) {
        try{
            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));

            if (slot.getAppointments() == null || slot.getProgramSessions() == null) {
                throw new IllegalArgumentException("Can't not update cause already have appointment or program sessions of this slot !");
            }

            slot.setSlotName(request.getSlotName());
            slot.setStartDateTime(request.getStartDateTime());
            slot.setEndDateTime(request.getEndDateTime());

            Slot savedSlot = slotRepository.save(slot);

            return Optional.of(this.mapToResponse(savedSlot));
        } catch (Exception e){
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
        } catch (Exception e){
            log.error("Failed to update slot status: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getAllSlotsByHostBy(Integer hostById) {
        try{
            List<Slot> slots = hostById != null ? slotRepository.findAllByHostedById(hostById)
                    : slotRepository.findAll();
            List<SlotResponse> responses = slots.stream().map(this::mapToResponse).toList();

            return Optional.of(responses);
        } catch (Exception e){
            log.error("Failed to view slot: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> getSlotById(Integer slotId) {
        try{
            Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new IllegalArgumentException("Slot not found"));
            SlotResponse response = mapToResponse(slot);

            return Optional.of(response);
        } catch (Exception e){
            log.error("Failed to view slot: {}", e.getMessage(), e);
            throw new RuntimeException("Something went wrong");
        }
    }


    private Slot mapToEntity(AddSlotRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Account account = accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Unauthorized"));

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
                .build();
    }
}
