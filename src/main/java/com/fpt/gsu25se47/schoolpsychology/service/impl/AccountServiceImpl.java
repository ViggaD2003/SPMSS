package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final StudentRepository studentRepository;
    private final CounselorRepository counselorRepository;
    private final TeacherRepository teacherRepository;
    private final GuardianRepository guardianRepository;
    private final ClassRepository classRepository;

    @Override
    public Optional<?> profileAccount() {
        Account account = getCurrentAccount();

        return switch (account.getRole().name().toUpperCase()) {
            case "STUDENT" -> Optional.of(getStudentDto(account));
            case "PARENTS" -> Optional.of(getParentDto(account));
            case "COUNSELOR" -> Optional.of(getCounselorDto(account));
            default -> Optional.of(getTeacherDto(account));
        };
    }

    @Override
    public List<?> listAllAccounts() {
        return accountRepository.findAll().stream()
                .filter(account -> !"MANAGER".equals(account.getRole().name()))
                .map(account -> switch (account.getRole().name().toUpperCase()) {
                    case "STUDENT" -> getStudentDto(account);
                    case "PARENTS" -> getParentDto(account);
                    case "COUNSELOR" -> getCounselorDto(account);
                    case "TEACHER" -> getTeacherDto(account);
                    default -> null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Optional<?> getAccountById(Integer id) throws BadRequestException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Not found account"));

        if (!"STUDENT".equals(account.getRole().name())) {
            throw new BadRequestException("Account Id is not Student");
        }
        return Optional.of(getStudentDto(account));
    }

    private StudentDto getStudentDto(Account account) {
        Student student = studentRepository.findById(account.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not found student"));

        StudentDto dto = new StudentDto();
        dto.setEmail(account.getEmail());
        dto.setDob(account.getDob());
        dto.setFullName(account.getFullName());
        dto.setGender(account.getGender());
        dto.setPhoneNumber(account.getPhoneNumber());
        dto.setStudentCode(student.getStudentCode());
        dto.setIsEnableSurvey(student.getIsEnableSurvey());

        Classes activeClass = classRepository.findActiveClassByStudentId(student.getId());
        if (activeClass != null) {
            ClassDto classDto = getClassDto(activeClass);
            dto.setClassDto(classDto);
        }
        return dto;
    }

    private static ClassDto getClassDto(Classes activeClass) {
        ClassDto classDto = new ClassDto();
        classDto.setClassYear(activeClass.getSchoolYear());
        classDto.setCodeClass(activeClass.getGrade().name() + activeClass.getCodeClass());

        if (activeClass.getTeacher() != null && activeClass.getTeacher().getAccount() != null) {
            TeacherOfClassDto teacherDto = new TeacherOfClassDto();
            Account teacherAcc = activeClass.getTeacher().getAccount();
            teacherDto.setEmail(teacherAcc.getEmail());
            teacherDto.setFullName(teacherAcc.getFullName());
            teacherDto.setPhoneNumber(teacherAcc.getPhoneNumber());
            teacherDto.setTeacherCode(activeClass.getTeacher().getTeacherCode());
            classDto.setTeacher(teacherDto);
        }
        return classDto;
    }

    private ParentDto getParentDto(Account account) {
        Guardian guardian = guardianRepository.findById(account.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not found guardian"));

        ParentDto dto = new ParentDto();
        dto.setEmail(account.getEmail());
        dto.setDob(account.getDob());
        dto.setFullName(account.getFullName());
        dto.setGender(account.getGender());
        dto.setPhoneNumber(account.getPhoneNumber());
        dto.setAddress(guardian.getAddress());

        List<StudentDto> students = guardian.getRelationships().stream()
                .map(Relationship::getStudent)
                .filter(Objects::nonNull)
                .map(this::mapStudentToDto)
                .filter(Objects::nonNull)
                .toList();

        RelationshipDto relationshipDto = new RelationshipDto();
        relationshipDto.setStudent(students);
        dto.setRelationships(relationshipDto);

        return dto;
    }

    private StudentDto mapStudentToDto(Student student) {
        Account studentAcc = accountRepository.findById(student.getId()).orElse(null);
        return studentAcc == null ? null : getStudentDto(studentAcc);
    }

    private CounselorDto getCounselorDto(Account account) {
        Counselor counselor = counselorRepository.findById(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Not found counselor"));

        CounselorDto counselorDto = new CounselorDto();
        counselorDto.setCounselorCode(counselor.getCounselorCode());
        counselorDto.setPhoneNumber(account.getPhoneNumber());
        counselorDto.setEmail(account.getEmail());
        counselorDto.setFullName(account.getFullName());
        counselorDto.setGender(account.getGender());
        counselorDto.setLinkMeet(counselor.getLinkMeet());

        return counselorDto;
    }

    private TeacherDto getTeacherDto(Account account) {
        Teacher teacher = teacherRepository.findById(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Not found teacher"));

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setEmail(account.getEmail());
        teacherDto.setFullName(account.getFullName());
        teacherDto.setPhoneNumber(account.getPhoneNumber());
        teacherDto.setGender(account.getGender());
        teacherDto.setTeacherCode(teacher.getTeacherCode());
        teacherDto.setLinkMeet(teacher.getLinkMeet());

        return teacherDto;
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
            List<InfoCounselor> infoCounselors = accounts.stream()
                    .map(this::mapToInfoCounselor)
                    .toList();
            return Optional.of(infoCounselors);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return Optional.of(e.getMessage());
        }
    }

    private InfoCounselor mapToInfoCounselor(Account account) {
        Counselor counselor = counselorRepository.findById(account.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Not found counselor"));

        return InfoCounselor.builder()
                .id(account.getId())
                .fullName(account.getFullName())
                .phoneNumber(account.getPhoneNumber())
                .gender(account.getGender())
                .dob(account.getDob())
                .counselorCode(counselor.getCounselorCode())
                .build();
    }
}
