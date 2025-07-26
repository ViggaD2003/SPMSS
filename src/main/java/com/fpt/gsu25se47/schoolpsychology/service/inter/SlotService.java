package com.fpt.gsu25se47.schoolpsychology.service.inter;
//
//import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Slot;
import com.fpt.gsu25se47.schoolpsychology.model.enums.SlotStatus;

import java.util.List;

public interface SlotService {
//
//    ResponseEntity<?> initSlot(List<AddSlotRequest> requests);
//
//    Optional<?> updateSlot(Integer slotId, UpdateSlotRequest request);
//
//    Optional<?> updateStatusSlot(Integer slotId);
//
    List<SlotResponse> getAllSlotsByHostBy(Integer hostById);
//
//    Optional<?> getSlotById(Integer slotId);
//
    Slot createSlot(CreateSlotRequest request);
    SlotResponse updateStatusSlot(Integer slotId, SlotStatus status);
}
