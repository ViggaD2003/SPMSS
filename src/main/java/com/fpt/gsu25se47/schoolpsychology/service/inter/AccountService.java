package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.CounselorDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<?> profileAccount();

    List<?> listAllAccounts();

    Optional<?> getAccountById(Integer id) throws BadRequestException;

    Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto);

    Optional<?> updateIsAbleSurvey(Integer accountId, Boolean isAbleSurvey) throws BadRequestException;

    Account getCurrentAccount();

    Optional<?> listAllCounselors();

    List<CounselorDto> getAllCounselors();
}
