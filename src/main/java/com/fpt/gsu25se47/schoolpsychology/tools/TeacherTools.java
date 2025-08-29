package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeacherTools {

    private final ClassService classService;
    private final AccountService accountService;

    @Tool(name = "getTeacherClasses",
            description = "Trả về danh sách các lớp của giáo viên hiện tại, từng lớp có các thông tin của lớp ở phần response trả về, " +
                    "khi được hỏi về các thông tin của từng lớp này, hãy đọc response hàm này trả về, từ đó đưa ra câu trả lời hoàn chỉnh," +
                    "chính xác, thân thiện, phong thái chuyên nghiệp")
    public List<ClassResponse> getMyClasses() {

        Account account = accountService.getCurrentAccount();
        return classService.getClassesByTeacherId(account.getId());
    }

    @Tool(description = "Get class details by code, this class details contains information of the term, the schoolYear, " +
            "the students in this class, each student have basic information, with isEnableSurvey which is a boolean value that " +
            "we can filter by this, and also we could filter other things like the information of student, and alos we could see the " +
            "latest survey record of this student")
    public ClassResponseSRC getClassByCode(String code) {

        return classService.getClassByCode(code);
    }
}
