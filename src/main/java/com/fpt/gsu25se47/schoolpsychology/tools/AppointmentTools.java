package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Appointment.AppointmentResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.enums.AppointmentStatus;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentTools {

    private final AccountService accountService;
    private final AppointmentService appointmentService;

    @Tool(
            name = "getAllAppointmentsByAccountWithStatuses",
            description = "Trả về danh sách các cuộc hẹn của tài khoản hiện tại theo trạng thái. " +
                    "Tool này sẽ trả về danh sách các cuộc hẹn của bạn (Account hiện tại) " +
                    "theo các trạng thái như " +
                    "PENDING -> trạng thái này dành cho các cuộc hẹn bị tạm hoãn, tạm ngưng lại" +
                    "CONFIRMED -> trạng thái này dành cho các cuộc hẹn đã xác nhận, và chưa tới ngày của cuộc hẹn để thực hiện cuộc hẹn, nghĩa là đây là các cuộc hẹn trong tương lai, sắp tới" +
                    "IN_PROGRESS -> trạng thái này dành cho các cuộc hẹn đang trong quá trình diễn ra buổi hẹn, tức là hiện tại đang diễn ra cuộc hẹn này" +
                    "CANCELED -> trạng thái này dành cho các cuộc hẹn đã bị huỷ" +
                    "COMPLETED -> trạng thái này dành cho các cuộc hẹn đã hoàn thành, kết thúc" +
                    "ABSENT. -> trạng thái này dành cho các trường hợp học sinh, người đặt không tham gia trong buổi hẹn" +
                    "Dữ liệu trả về human-friendly, không bao gồm thông tin nhạy cảm."
    )
    public List<AppointmentResponse> getAllAccAppointmentsByStatuses(List<AppointmentStatus> statuses) {

        Account account = accountService.getCurrentAccount();
        return appointmentService.getAllAccAppointmentsByStatuses(account.getId(), statuses);
    }

}
