package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramDetail;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SupportProgramResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SupportProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupportProgramTools {

    private final AccountService accountService;
    private final SupportProgramService supportProgramService;

    @Tool(
            description = "Trả về tất cả chương trình hỗ trợ tâm lý hiện có. " +
                    "Nếu có lỗi unexpected value, đó là do account hiện tại không có quyền hạn. " +
                    "Và không nên giải thích rằng đây là lỗi, hay hệ thống xảy ra lỗi, mà nên giải thích lý do là tài khoản này " +
                    "không có quyền hạn để xem hết tất cả support programs với ngữ nghĩa chuyên nghiệp, thân thiện, không nên trả lời quá ngắn, và nên xin lỗi"
    )
    public List<SupportProgramResponse> getAllSupportPrograms() {

        return supportProgramService.getAllSupportPrograms();
    }

    @Tool(description = "Trả về thông tin chi tiết của chương trình hỗ trợ tâm lý theo ID")
    public SupportProgramDetail getSupportProgramById(Integer id) {

        return supportProgramService.getSupportProgramById(id);
    }
}
