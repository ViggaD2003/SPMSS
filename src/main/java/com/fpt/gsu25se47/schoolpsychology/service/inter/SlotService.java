package com.fpt.gsu25se47.schoolpsychology.service.inter;
//
//import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;

import java.util.List;

public interface SlotService {

    SlotResponse updateSlot(Integer slotId, UpdateSlotRequest request);
    List<SlotResponse> getAllSlotsByHostBy(Integer hostById);
    List<SlotResponse> createSlots(List<CreateSlotRequest> request);
    SlotResponse updateStatusSlot(Integer slotId, SlotStatus status);
}
