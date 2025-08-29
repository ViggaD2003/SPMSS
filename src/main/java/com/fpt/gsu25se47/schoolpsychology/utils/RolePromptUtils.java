package com.fpt.gsu25se47.schoolpsychology.utils;

import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RolePromptUtils {

    @Value("classpath:/promptTemplates/adminCommunicationStylePrompt.st")
    Resource adminComStylePrompt;

    @Value("classpath:/promptTemplates/counselorCommunicationStylePrompt.st")
    Resource counselorComStylePrompt;

    @Value("classpath:/promptTemplates/parentsCommunicationStylePrompt.st")
    Resource parentsComStylePrompt;

    @Value("classpath:/promptTemplates/studentCommunicationStylePrompt.st")
    Resource studentComStylePrompt;

    @Value("classpath:/promptTemplates/teacherCommunicationStylePrompt.st")
    Resource teacherComStylePrompt;

    private static final Map<Role, String> COMMUNICATION_STYLE_CACHE = new ConcurrentHashMap<>();

    public String buildCommunicationStyle(Role role) {
        return COMMUNICATION_STYLE_CACHE.computeIfAbsent(role, r -> {
            try {
                Resource resource = switch (r) {
                    case STUDENT -> studentComStylePrompt;
                    case PARENTS -> parentsComStylePrompt;
                    case TEACHER -> teacherComStylePrompt;
                    case COUNSELOR -> counselorComStylePrompt;
                    case MANAGER -> adminComStylePrompt;
                };
                return resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load communication style for role: " + r, e);
            }
        });
    }

    public String buildDataAccessRules(Role role) {
        return switch (role) {
            case STUDENT -> """
                    **QUY TẮC TRUY CẬP - HỌC SINH**:
                    - Được xem: Thông tin cá nhân, kết quả khảo sát của mình, tiến trình tư vấn cá nhân
                    - Không được xem: Thông tin học sinh khác, dữ liệu hệ thống nội bộ, hồ sơ y tế chi tiết
                    - Bảo mật: Tất cả thông tin cá nhân được bảo vệ nghiêm ngặt
                    """;

            case PARENTS -> """
                    **QUY TẮC TRUY CẬP - PHỤ HUYNH**:
                    - Được xem: Thông tin con em mình, báo cáo tiến trình, khuyến nghị hỗ trợ tại nhà
                    - Không được xem: Thông tin chi tiết các học sinh khác, nội dung tư vấn riêng tư nếu con từ chối chia sẻ
                    - Bảo mật: Tôn trọng quyền riêng tư phù hợp độ tuổi của học sinh
                    """;

            case TEACHER -> """
                    **QUY TẮC TRUY CẬP - GIÁO VIÊN**:
                    - Được xem: Thông tin cơ bản học sinh trong lớp, gợi ý hỗ trợ học tập, chiến lược quản lý lớp
                    - Không được xem: Nội dung tư vấn chi tiết, chẩn đoán tâm lý, thông tin y tế nhạy cảm
                    - Bảo mật: Chỉ thông tin cần thiết cho việc giảng dạy và hỗ trợ học tập
                    """;

            case COUNSELOR -> """
                    **QUY TẮC TRUY CẬP - CHUYÊN VIÊN TƯ VẤN**:
                    - Được xem: Hồ sơ đầy đủ học sinh được phân công, lịch sử tư vấn, kết quả đánh giá tâm lý, kế hoạch can thiệp
                    - Không được xem: Thông tin học sinh ngoài trách nhiệm, dữ liệu hệ thống kỹ thuật
                    - Bảo mật: Tuân thủ đạo đức nghề nghiệp tư vấn tâm lý
                    """;

            case MANAGER -> """
                    **QUY TẮC TRUY CẬP - QUẢN LÝ**:
                    - Được xem: Báo cáo thống kê tổng hợp, xu hướng toàn trường, dữ liệu phân tích hệ thống
                    - Không được xem: Nội dung tư vấn cá nhân chi tiết (trừ trường hợp khẩn cấp có văn bản)
                    - Bảo mật: Truy cập dữ liệu phải có lý do chính đáng và tuân thủ quy định
                    """;
        };
    }
}
