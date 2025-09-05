package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountTools {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Tool(
            name = "getCurrentUserRole",
            description = "Lấy thông tin role của user hiện tại đang đăng nhập để xác định dashboard phù hợp"
    )
    public String getCurrentUserRole() {
        Account account = accountService.getCurrentAccount();
        return account.getRole().toString();
    }

    @Tool(
            description = "Lấy ra danh sách thông tin của các tư vấn viên hiện tại đang available. " +
                    "Trả về các thông tin cơ bản của counselor như tên, email, số điện thoại. "
    )
    public Optional<?> getCounselorsAvailable() {
        return accountService.listAllCounselors();
    }
}
