package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;

import java.util.Optional;

public interface AccountService {
    Optional<?> profileAccount();

    Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto);
    Account getCurrentAccount();
}
