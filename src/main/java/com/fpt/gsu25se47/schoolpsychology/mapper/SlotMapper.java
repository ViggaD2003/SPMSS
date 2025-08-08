package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.BookedSlot;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Appointment;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SlotMapper.class})
public interface SlotMapper {

    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    Slot toSlot(CreateSlotRequest request,
                @Context Account account);

    @Mapping(target = "fullName", source = "hostedBy.fullName")
    @Mapping(target = "roleName", source = "hostedBy.role")
    @Mapping(target = "status", expression = "java(slot.getStatus().name())")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    SlotResponse toSlotResponse(Slot slot, @Context List<Appointment> appointments);

    @Mapping(target = "fullName", source = "hostedBy.fullName")
    @Mapping(target = "roleName", source = "hostedBy.role")
    @Mapping(target = "booked", ignore = true)
    SlotResponse toSlotResponseWithoutAppointments(Slot slot);

    Slot updateSlotFromRequest(UpdateSlotRequest request,
                               @MappingTarget Slot slot);

    @AfterMapping
    default void setAccountToSlot(@MappingTarget Slot slot, @Context Account account) {
        slot.setHostedBy(account);
    }

    @AfterMapping
    default void setBookedToSlotResponse(@MappingTarget SlotResponse slotResponse, @Context List<Appointment> appointments) {
        List<BookedSlot> booked = slotResponse.getBooked();
        appointments.forEach(a -> {
            if (a.getStatus() != AppointmentStatus.CANCELED) {
                booked.add(BookedSlot.builder()
                        .startDateTime(a.getStartDateTime())
                        .endDateTime(a.getEndDateTime())
                        .build());
            }
        });
    }
}