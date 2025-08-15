package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    UserDetailsService userDetailsService();

    Optional<?> profileAccount();

    List<?> listAllAccounts(Role role, Integer classId, Grade grade);

    Optional<?> getAccountById(Integer id) throws BadRequestException;

    Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto);

    Optional<?> updateIsAbleSurvey(Integer accountId, Boolean isAbleSurvey) throws BadRequestException;

    Account getCurrentAccount();

    Optional<?> listAllCounselors();

    List<StudentSRCResponse> getStudentsByClassWithLSR(Integer classId);

    List<StudentDto> getEligibleStudents(Integer classId);
}
