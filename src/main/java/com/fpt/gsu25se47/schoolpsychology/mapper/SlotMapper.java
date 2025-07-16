package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SlotMapper {

    @Mapping(target = "id", ignore = true)
    @BeanMapping(builder = @Builder(disableBuilder = true))
    Slot toSlot(CreateSlotRequest request,
                @Context Account account);

    @Mapping(target = "slotType", source = "type")
    @Mapping(target = "updatedAt", source = "updatedDate")
    @Mapping(target = "createdAt", source = "createdDate")
    @Mapping(target = "fullName", source = "hostedBy.fullName")
    @Mapping(target = "roleName", source = "hostedBy.role")
    SlotResponse toSlotResponse(Slot slot);

    @AfterMapping
    default void setAccountToSlot(@MappingTarget Slot slot, @Context Account account) {
        slot.setHostedBy(account);
    }
}
