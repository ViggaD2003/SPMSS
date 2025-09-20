package com.fpt.gsu25se47.schoolpsychology.service.inter;


import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import com.fpt.gsu25se47.schoolpsychology.model.enums.RelationshipType;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    UserDetailsService userDetailsService();

    Optional<?> profileAccount();

    List<?> listAllAccounts(Role role, Integer classId, Grade grade);

    List<?> listAllAccounts();

    Optional<?> getAccountById(Integer id) throws BadRequestException;

    Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto);

    Optional<?> updateIsAbleSurvey(Integer accountId, Boolean isAbleSurvey) throws BadRequestException;

    Account getCurrentAccount();

    Optional<?> listAllCounselors();

    List<StudentSRCResponse> getStudentsByClassWithLSR(Integer classId);

    List<StudentDto> getEligibleStudents(Integer classId, Grade grade, Boolean gender);

    List<TeacherDto> getEligibleTeachers(Integer classId);

    StudentDetailResponse getStudentDetails(Integer studentId);

    String linkRelationship(Integer parentId, List<Integer> childIds, RelationshipType relationshipType);

    String removeRelationship(Integer parentId, List<Integer> childIds);

    String enableStatusAccount(Integer accountId, Boolean status);
}
