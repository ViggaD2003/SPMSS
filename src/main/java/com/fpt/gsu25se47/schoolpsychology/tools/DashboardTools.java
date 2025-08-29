package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.CounselorDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.ManagerDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.MangerAndCounselor.StudentDashboard;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Dashboard.Teacher.TeacherDashboardResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.DashBoardService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.TeacherDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DashboardTools {

    private final AccountService accountService;
    private final DashBoardService dashBoardService;
    private final TeacherDashboardService teacherDashboardService;

    @Tool(
            name = "getManagerDashboard",
            description = "Trả về bảng thống kê tổng quan dành cho quản lý với số liệu toàn trường. " +
                    "CHỈ dành cho MANAGER role." +
                    "KHI HIỂN THỊ: " +
                    "- Format: '📊 **Bảng Thống Kê Quản Lý**' " +
                    "- Nhóm thông tin: Học sinh, Chương trình, Chuyên viên, Khảo sát " +
                    "- Sử dụng emoji: 👥📈📉⚠️✅ " +
                    "- Highlight các chỉ số quan trọng và cảnh báo " +
                    "- Kết thúc: 'Bạn muốn xem chi tiết phần nào?' " +
                    "Nếu không có quyền: Xin lỗi, chức năng này chỉ dành cho quản lý cấp cao."
    )
    public ManagerDashboard getManagerDashboard() {
        return dashBoardService.managerDashboard();
    }

    @Tool(
            name = "getCounselorDashboard",
            description = "Trả về bảng thống kê dành cho chuyên viên tư vấn hiện tại đang đăng nhập với thông tin học sinh được phân công. " +
                    "CHỈ dành cho COUNSELOR role." +
                    "KHI HIỂN THỊ: " +
                    "- Format: '🧠 **Bảng Thống Kê Tư Vấn**' " +
                    "- Nhóm theo: Học sinh cần quan tâm, Lịch hẹn, Báo cáo mới " +
                    "- Ưu tiên hiển thị: Cases khẩn cấp, deadline sắp tới " +
                    "- Sử dụng emoji: 🔴⚠️📅👤📝 " +
                    "- Kết thúc: 'Bạn muốn xem chi tiết học sinh nào trước?' " +
                    "Nếu không có quyền: Xin lỗi, chức năng này chỉ dành cho chuyên viên tư vấn."
    )
    public CounselorDashboard getCounselorDashboard() {
        return dashBoardService.counselorDashboard();
    }

    @Tool(
            name = "getStudentDashboard",
            description = "Trả về bảng thống kê cá nhân của học sinh trong khoảng thời gian cụ thể. " +
                    "Quyền: STUDENT (chỉ xem của mình), PARENTS (xem con)." +
                    "KHI HIỂN THỊ: " +
                    "- Format: '📚 **Bảng Thống Kê Cá Nhân - [Tên học sinh]**' " +
                    "- Nhóm theo: Chương trình tham gia, Tiến độ, Khảo sát hoàn thành " +
                    "- Sử dụng progress bar emoji: ▓▓▓░░ hoặc 🟢🟡🔴 " +
                    "- Hiển thị thành tích tích cực và động lực " +
                    "- Kết thúc: 'Bạn muốn tìm hiểu thêm về phần nào?' " +
                    "Nếu không có quyền: Xin lỗi và giải thích quy định bảo mật."
    )
    public StudentDashboard getStudentDashboard(
            @ToolParam(description = "Ngày bắt đầu (YYYY-MM-DD)")
            LocalDate startDate,
            @ToolParam(description = "Ngày kết thúc (YYYY-MM-DD)")
            LocalDate endDate,
            @ToolParam(description = "ID của học sinh cần xem thống kê")
            Integer studentId) {
        Account account = accountService.getCurrentAccount();
        if (account.getRole() != Role.STUDENT && account.getRole() != Role.PARENTS) {
            throw new AuthorizationDeniedException("Chỉ có student và parents mới sử dụng được chức năng này");
        }
        return dashBoardService.studentDashboard(startDate, endDate, studentId);
    }

    @Tool(
            name = "getTeacherDashboard",
            description = "Trả về bảng thống kê dành cho giáo viên hiện tại đang đăng nhập với thông tin học sinh trong lớp được phân công. " +
                    "CHỈ dành cho TEACHER role." +
                    "KHI HIỂN THỊ: " +
                    "- Format: '👩‍🏫 **Bảng Thống Kê Giáo Viên**' " +
                    "- Nhóm theo: Học sinh cần quan tâm trong lớp, Tình hình học tập, Vấn đề hành vi " +
                    "- Ưu tiên hiển thị: Học sinh có dấu hiệu bất thường, cần hỗ trợ " +
                    "- Sử dụng emoji: 📚👤⚠️📊🟢🟡🔴 " +
                    "- Hiển thị xu hướng và so sánh với kỳ trước " +
                    "- Kết thúc: 'Bạn muốn tìm hiểu thêm về học sinh nào?' " +
                    "Nếu không có quyền: Xin lỗi, chức năng này chỉ dành cho giáo viên."
    )
    public TeacherDashboardResponse getTeacherDashboard() {
        return teacherDashboardService.getTeacherDashboardResponse();
    }
}
