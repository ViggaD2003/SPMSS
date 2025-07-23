//package com.fpt.gsu25se47.schoolpsychology.service.inter;
//
//import com.fpt.gsu25se47.schoolpsychology.dto.request.AddSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateSlotRequest;
//import com.fpt.gsu25se47.schoolpsychology.model.Slot;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface SlotService {
//
//    ResponseEntity<?> initSlot(List<AddSlotRequest> requests);
//
//    Optional<?> updateSlot(Integer slotId, UpdateSlotRequest request);
//
//    Optional<?> updateStatusSlot(Integer slotId);
//
//    Optional<?> getAllSlotsByHostBy(Integer hostById);
//
//    Optional<?> getSlotById(Integer slotId);
//
//    Slot createSlot(CreateSlotRequest request);
//}
