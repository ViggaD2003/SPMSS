package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramDetail;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramPPResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupportProgramTools {

    private final AccountService accountService;
    private final SupportProgramService supportProgramService;

    @Tool(
            description = "Trả về danh sách các chương trình hỗ trợ tâm lý. " +
                    "KHI HIỂN THI KẾT QUẢ: " +
                    "- Sử dụng format: '📋 **[Tên program]** (ID: [id]) - [thông tin khác]' " +
                    "- Luôn hiển thị ID trong ngoặc đơn sau tên để user có thể tham chiếu " +
                    "- Mỗi chương trình hiển thị: ID, tên, thời gian, địa điểm, người phụ trách " +
                    "- Kết thúc bằng 'Bạn muốn xem chi tiết chương trình nào?' " +
                    "Nếu có lỗi: giải thích tài khoản không có quyền truy cập với lời xin lỗi."
    )
    public List<SupportProgramResponse> getAllSupportPrograms() {

        return supportProgramService.getAllSupportPrograms();
    }

    @Tool(
            description = "Trả về thông tin chi tiết của chương trình hỗ trợ tâm lý. " +
                    "Có thể tìm bằng ID (số) hoặc tên chương trình (chuỗi). " +
                    "Ví dụ: getSupportProgramDetails(5) hoặc getSupportProgramDetails('Chương trình ABC')"
    )
    public SupportProgramDetail getSupportProgramById(String identifier) {

        try {
            Integer id = Integer.parseInt(identifier);
            return supportProgramService.getSupportProgramById(id);
        } catch (NumberFormatException e) {
            List<SupportProgramResponse> programs = supportProgramService.getAllSupportPrograms();
            Integer foundId = programs.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(identifier.trim()) ||
                            p.getName().toLowerCase().contains(identifier.toLowerCase().trim()))
                    .map(SupportProgramResponse::getId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy chương trình với tên: " + identifier +
                                    "Vui lòng kiểm tra lại tên chương trình"
                    ));

            return supportProgramService.getSupportProgramById(foundId);
        }
    }

    @Tool(
            name = "getRecommendedSupportPrograms",
            description = "Trả về danh sách các chương trình hỗ trợ tâm lý được đề xuất cho học sinh cụ thể " +
                    "dựa trên tình trạng sức khỏe tâm lý hoặc kết quả khảo sát của học sinh đó. " +
                    "CHỈ sử dụng khi user hỏi về chương trình phù hợp/đề xuất cho học sinh cụ thể. " +
                    "KHI HIỂN THỊ KẾT QUẢ: " +
                    "- Format: '🎯 **Đề xuất cho [Tên học sinh]**' " +
                    "- Mỗi chương trình: '⭐ **[Tên]** - [Lý do đề xuất ngắn gọn]' " +
                    "- Giải thích tại sao chương trình này phù hợp với học sinh " +
                    "- Sắp xếp theo độ ưu tiên/phù hợp " +
                    "Lưu ý: Chỉ STUDENT role có thể sử dụng."
    )
    public List<SupportProgramResponse> getSuggestSupportProgram() {

        Account account = accountService.getCurrentAccount();
        if (account.getRole() != Role.STUDENT) {
            throw new AuthorizationDeniedException("Chỉ có student mới sử dụng được chức năng này");
        }
        return supportProgramService.getSuggestSupportProgram(account.getId());
    }

    @Tool(
            name = "getStudentSupportPrograms",
            description = "Trả về danh sách các chương trình hỗ trợ tâm lý mà học sinh đang tham gia hoặc đã tham gia. " +
                    "CHỈ sử dụng khi user hỏi về các chương trình của học sinh cụ thể. " +
                    "KHI HIỂN THỊ KẾT QUẢ: " +
                    "- Format: '👨‍🎓 **Chương trình của [Tên học sinh]**' " +
                    "- Mỗi chương trình: '📚 **[Tên]** - [Trạng thái tham gia] | ⏰ [Thời gian]' " +
                    "- Nhóm theo trạng thái: Đang tham gia / Đã hoàn thành / Sắp tới " +
                    "- Hiển thị tiến độ hoặc kết quả nếu có " +
                    "- Kết thúc: 'Bạn muốn xem chi tiết chương trình nào?' " +
                    "Quyền truy cập: STUDENT (chỉ xem của chính mình), PARENTS (xem của con), "
    )
    public List<SupportProgramPPResponse> getStudentSupportPrograms(
            @ToolParam(description = "ID của học sinh cần xem danh sách chương trình")
            Integer studentId) {
        Account account = accountService.getCurrentAccount();
        if (account.getRole() != Role.STUDENT && account.getRole() != Role.PARENTS) {
            throw new AuthorizationDeniedException("Chỉ có student và parents mới sử dụng được chức năng này");
        }
        return supportProgramService.getSupportProgramsByStudentId(studentId);
    }
}
