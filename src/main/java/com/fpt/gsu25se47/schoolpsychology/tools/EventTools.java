package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.EventResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventTools {

    private final EventService eventService;

    @Tool(description = "Trả về danh sách các sự kiện của học sinh hiện tại trong khoảng thời gian nhất định, " +
            "nếu khoảng thời gian startDate và endDate không được đề cập, tự động hiểu trả về danh sách các sự kiện trong ngày hôm nay." +
            "Chỉ dành cho Role: STUDENT")
    public List<EventResponse> getEvents(
            @ToolParam(description = "Id của student hiện tại đang đăng nhập") Integer studentId,
            @ToolParam(description = "Ngày bắt đầu") LocalDate startDate,
            @ToolParam(description = "Ngày kết thúc") LocalDate endDate
    ) {
        return eventService.getEvents(studentId, startDate, endDate);
    }
}
