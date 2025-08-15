package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.AccountDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Parent.ParentBaseResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyRecordGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.TeacherDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Grade;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CaseRepository caseRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;
    private final SurveyRecordMapper surveyRecordMapper;
    private final AccountMapper accountMapper;
    private final TeacherMapper teacherMapper;
    private final ParentMapperInf parentMapperInf;
    private final CaseMapper caseMapper;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return accountRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
            }
        };
    }

    @Override
    public Optional<?> profileAccount() {
        Account account = getCurrentAccount();
        return Optional.of(accountMapper.toDto(account));
    }

    @Override
    public List<?> listAllAccounts(Role role, Integer classId, Grade grade) {
        return accountRepository.findAccountsByRoleNative(role == null ? null : role.name(), classId, grade == null ? null : grade.name()).stream()
                .filter(account -> !"MANAGER".equals(account.getRole().name()))
                .map(account -> Optional.of(accountMapper.toDto(account)))
                .toList();
    }

    @Override
    public Optional<?> getAccountById(Integer id) throws BadRequestException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found account"));

        if (!"STUDENT".equals(account.getRole().name())) {
            throw new BadRequestException("Account Id is not Student");
        }
        return Optional.of(accountMapper.toDto(account));
    }

    @Override
    public Optional<?> updateProfileAccount(UpdateProfileDto dto) {
        Account account = getCurrentAccount();
        account.setFullName(dto.getFullName());
        account.setGender(dto.getGender());
        account.setPhoneNumber(dto.getPhoneNumber());
        account.setDob(dto.getDob());
        accountRepository.save(account);

        return profileAccount();
    }

    @Override
    public Optional<?> updateIsAbleSurvey(Integer accountId, Boolean isAbleSurvey) throws BadRequestException {
        Student student = studentRepository.findById(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("Not found student"));
        student.setIsEnableSurvey(isAbleSurvey);
        studentRepository.save(student);
        return Optional.of(student);
    }

    @Override
    public Account getCurrentAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new BadCredentialsException("Invalid authentication principal");
        }

        return accountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Account not found for email: " + userDetails.getUsername()));
    }

    @Override
    public Optional<?> listAllCounselors() {
        try {
            List<Account> accounts = accountRepository.findCounselorsWithSlots();
            List<AccountDto> infoCounselors = accounts.stream()
                    .map(acc -> accountMapper.toDto(acc))
                    .toList();
            return Optional.of(infoCounselors);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return Optional.of(e.getMessage());
        }
    }

    @Override
    public List<StudentSRCResponse> getStudentsByClassWithLSR(Integer classId) {

        List<Student> students = enrollmentRepository.findStudentsByClassesId(classId);

        return students.stream()
                .map(student -> {
                    Optional<SurveyRecord> latestRecord = student.getAccount()
                            .getSurveyRecords()
                            .stream()
                            .max(Comparator.comparing(SurveyRecord::getCompletedAt));

                    List<Cases> cases = caseRepository.findAllCasesByRoleAndAccountWithStatusSorted(Role.STUDENT.name(), student.getId(), Collections.emptyList(), 0, null);
                    List<Integer> caseIds = cases.stream().map(Cases::getId).toList();
                    StudentSRCResponse studentSRCResponse = studentMapper.toStudentSrcResponse(student, caseIds);

                    latestRecord.ifPresent(sr -> {
                        SurveyRecordGetAllResponse sre = surveyRecordMapper.mapToSurveyRecordGetAllResponse(sr);
                        studentSRCResponse.setLatestSurveyRecord(sre);
                    });

                    return studentSRCResponse;
                })
                .toList();
    }

    @Override
    public List<StudentDto> getEligibleStudents(Integer classId) {

        Classes classes = classRepository.findById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + classId));

        List<Student> students = studentRepository.findEligibleStudents(classes.getId());

        return students
                .stream()
                .map(studentMapper::mapStudentDtoWithoutClass)
                .toList();
    }

    @Override
    public List<TeacherDto> getEligibleTeachers(Integer classId) {
        Classes classes = classRepository.findById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + classId));

        List<Teacher> teachers = teacherRepository.findEligibleStudents(classes.getId());

        return teachers
                .stream()
                .map(teacherMapper::toTeacherOfClassDto)
                .toList();
    }

    @Override
    public StudentDetailResponse getStudentDetails(Integer studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student not found for ID: " + studentId));

        List<Cases> caseGetAllForStudentResponses = caseRepository.findAllCasesByRoleAndAccountWithStatusSorted(Role.STUDENT.name(), studentId, Collections.emptyList(), 0, null);
        List<CaseGetAllForStudentResponse> cases =  caseGetAllForStudentResponses.stream()
                .map(c -> caseMapper.mapToCaseGetAllForStudentResponse(c, null))
                .toList();

        List<Guardian> parents = student.getRelationships()
                .stream()
                .filter(s -> s.getStudent().getId().equals(studentId))
                .map(Relationship::getGuardian)
                .toList();

        List<ParentBaseResponse> parentBaseResponses = parents
                .stream()
                .map(parentMapperInf::toParentBaseResponse)
                .toList();

        return studentMapper.toStudentDetailResponse(student, cases, parentBaseResponses);
    }
}
