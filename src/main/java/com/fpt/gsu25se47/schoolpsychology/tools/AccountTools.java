package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTools {

    private final AccountService accountService;

    @Tool(
            name = "getCurrentUserRole",
            description = "Lấy thông tin role của user hiện tại đang đăng nhập để xác định dashboard phù hợp"
    )
    public String getCurrentUserRole() {
        Account account = accountService.getCurrentAccount();
        return account.getRole().toString();
    }
}
