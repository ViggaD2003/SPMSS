package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SlotResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.SlotMapper;
import com.fpt.gsu25se47.schoolpsychology.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SlotTools {

    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;

    @Tool(
            description = """
                    Lấy ra danh sách slot hiện tại đang available
    Tham số:
    - now: thời gian hiện tại được tính làm mốc
    - bufferMinutes: khoảng thời gian từ thời điểm now đến thời gian bắt đầu slot (mặc định 30 phút).
    - maxHours: khoảng thời gian tối đa cần tìm kiếm slot (mặc định 8 tiếng).
    """
    )
    public List<SlotResponse> getSlotsAvailable(String now, int bufferMinutes, int maxHours) {
        LocalDateTime parsed = LocalDateTime.parse(now, DateTimeFormatter.ISO_DATE_TIME);

        return slotRepository.findAvailableSlots(parsed, bufferMinutes, maxHours)
                .stream()
                .map(s -> slotMapper.toSlotResponse(s, s.getAppointments()))
                .toList();
    }

    @Tool(
            description = """
    Lấy ra danh sách slot hiện tại đang available theo tên của tư vấn viên.
    Tham số:
    - now: thời gian hiện tại được tính làm mốc
    - bufferMinutes: khoảng thời gian từ thời điểm now đến thời gian bắt đầu slot (tự động lấy mặc định 30 phút, không cần hỏi user, nếu user có yêu cầu khoảng thời gian này thì sẽ lấy theo user yêu cầu).
    - maxHours: khoảng thời gian tối đa cần tìm kiếm slot (tự động lấy mặc định 8 tiếng, không cần hỏi user, nếu user có yêu cầu khoảng thời gian này thì sẽ lấy theo user yêu cầu, khi trả lời user nhớ đề cập).
    - name: tên tư vấn viên.
    """
    )
    public List<SlotResponse> getSlotsAvailableByHostName(
            String now,
            int bufferMinutes,
            int maxHours,
            String name) {

        LocalDateTime parsed = LocalDateTime.parse(now, DateTimeFormatter.ISO_DATE_TIME);

        return slotRepository.findAvailableSlotsByHostName(parsed, bufferMinutes, maxHours, name)
                .stream()
                .map(s -> slotMapper.toSlotResponse(s, s.getAppointments()))
                .toList();
    }

}
