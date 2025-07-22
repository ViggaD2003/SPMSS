package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.mapper.CounselorMapper;
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
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final StudentRepository studentRepository;

    private final CounselorRepository counselorRepository;

    private final TeacherRepository teacherRepository;

    private final GuardianRepository guardianRepository;

    private final CounselorMapper counselorMapper;

    @Override
    public Optional<?> profileAccount() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if (account == null) {
                throw new UsernameNotFoundException("Not found user");
            }

            return switch (account.getRole().name().toUpperCase()) {
                case "STUDENT" -> Optional.of(getStudentDto(account));
                case "PARENTS" -> Optional.of(getParentDto(account));
                case "COUNSELOR" -> Optional.of(getCounselorDto(account));
                default -> Optional.of(getTeacherDto(account));
            };

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public List<?> listAllAccounts() {
        List<Account> accounts = accountRepository.findAll().stream().filter(account -> !account.getRole().name().equals("MANAGER")).toList();

        return accounts.stream()
                .map(account -> switch (account.getRole().name().toUpperCase()) {
                    case "STUDENT" -> getStudentDto(account);
                    case "PARENTS" -> getParentDto(account);
                    case "COUNSELOR" -> getCounselorDto(account);
                    case "TEACHER" -> getTeacherDto(account);
                    default -> null;
                })
                .toList();
    }

    @Override
    public Optional<?> getAccountById(Integer id) throws BadRequestException {
        Account account = accountRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Not found account"));
        if (!account.getRole().name().equals("STUDENT")) {
            throw new BadRequestException("Account Id is not Student");
        }
        return Optional.of(getStudentDto(account));
    }


    private StudentDto getStudentDto(Account account) {
        Student student = studentRepository.findById(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Not found student"));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account loginAccountInTheMoment = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);

        StudentDto studentDto = new StudentDto();
        studentDto.setEmail(account.getEmail());
        studentDto.setDob(account.getDob());
        studentDto.setFullName(account.getFullName());
        studentDto.setGender(account.getGender());
        studentDto.setIsEnableSurvey(student.getIsEnableSurvey());
        studentDto.setPhoneNumber(account.getPhoneNumber());
        studentDto.setStudentCode(student.getStudentCode());

        if (student.getClasses() != null) {
            ClassDto classDto = new ClassDto();
            classDto.setClassYear(student.getClasses().getClassYear());
            classDto.setCodeClass(student.getClasses().getCodeClass());

            if (student.getClasses().getTeacher() != null && student.getClasses().getTeacher().getAccount() != null) {
                TeacherOfClassDto teacherDto = new TeacherOfClassDto();
                teacherDto.setEmail(student.getClasses().getTeacher().getAccount().getEmail());
                teacherDto.setFullName(student.getClasses().getTeacher().getAccount().getFullName());
                teacherDto.setPhoneNumber(student.getClasses().getTeacher().getAccount().getPhoneNumber());
                teacherDto.setTeacherCode(student.getClasses().getTeacher().getTeacherCode());
                classDto.setTeacher(teacherDto);
            }

            studentDto.setClassDto(classDto);
        }

        if (!loginAccountInTheMoment.getRole().name().equals("MANAGER")) {
            List<MentalEvaluationDto> mentalEvaluationDtos = student.getMentalEvaluations().stream()
                    .map(this::mapToMentalEvaluationDto).toList();
            studentDto.setMentalEvaluations(mentalEvaluationDtos);
        }
        return studentDto;
    }

    private MentalEvaluationDto mapToMentalEvaluationDto(MentalEvaluation mentalEvaluation) {
        return MentalEvaluationDto.builder()
                .id(mentalEvaluation.getId())
                .evaluationType(mentalEvaluation.getEvaluationType().name())
                .appointmentRecordId(mentalEvaluation.getAppointmentRecord() != null ? mentalEvaluation.getAppointmentRecord().getId() : null)
                .programRecordId(mentalEvaluation.getProgramRecord() != null ? mentalEvaluation.getProgramRecord().getId() : null)
                .surveyRecordId(mentalEvaluation.getSurveyRecord() != null ? mentalEvaluation.getSurveyRecord().getId() : null)
                .date(mentalEvaluation.getDate())
                .totalScore(mentalEvaluation.getTotalScore())
                .categoryResponse(CategoryResponse.builder()
                        .name(mentalEvaluation.getCategory().getName())
                        .id(mentalEvaluation.getCategory().getId())
                        .code(mentalEvaluation.getCategory().getCode())
                        .build())
                .build();
    }

    private ParentDto getParentDto(Account account) {
        Guardian guardian = guardianRepository.findById(account.getId()).orElseThrow(() -> new UsernameNotFoundException("Not found guardian"));

        ParentDto parentDto = new ParentDto();
        parentDto.setEmail(account.getEmail());
        parentDto.setDob(account.getDob());
        parentDto.setFullName(account.getFullName());
        parentDto.setGender(account.getGender());
        parentDto.setPhoneNumber(account.getPhoneNumber());
        parentDto.setAddress(guardian.getAddress());

        List<StudentDto> studentDtos = guardian.getRelationships().stream()
                .map(Relationship::getStudent)
                .filter(Objects::nonNull)
                .map(this::mapStudentToDto)
                .filter(Objects::nonNull)
                .toList();

        RelationshipDto relationshipDto = new RelationshipDto();
        relationshipDto.setStudent(studentDtos);
        parentDto.setRelationships(relationshipDto);

        return parentDto;
    }

    private StudentDto mapStudentToDto(Student student) {
        Account studentAcc = accountRepository.findById(student.getId()).orElse(null);
        if (studentAcc == null) return null;

        StudentDto studentDto = new StudentDto();
        studentDto.setEmail(studentAcc.getEmail());
        studentDto.setDob(studentAcc.getDob());
        studentDto.setFullName(studentAcc.getFullName());
        studentDto.setGender(studentAcc.getGender());
        studentDto.setPhoneNumber(studentAcc.getPhoneNumber());
        studentDto.setStudentCode(student.getStudentCode());
        studentDto.setIsEnableSurvey(student.getIsEnableSurvey());

        if (student.getClasses() != null) {
            ClassDto classDto = new ClassDto();
            classDto.setClassYear(student.getClasses().getClassYear());
            classDto.setCodeClass(student.getClasses().getCodeClass());

            if (student.getClasses().getTeacher() != null && student.getClasses().getTeacher().getAccount() != null) {
                TeacherOfClassDto teacherDto = new TeacherOfClassDto();
                teacherDto.setEmail(student.getClasses().getTeacher().getAccount().getEmail());
                teacherDto.setFullName(student.getClasses().getTeacher().getAccount().getFullName());
                teacherDto.setPhoneNumber(student.getClasses().getTeacher().getAccount().getPhoneNumber());
                teacherDto.setTeacherCode(student.getClasses().getTeacher().getTeacherCode());
                classDto.setTeacher(teacherDto);
            }

            studentDto.setClassDto(classDto);
        }

        return studentDto;
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
    public Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if (account == null) {
                throw new UsernameNotFoundException("Not found account");
            }

            account.setFullName(updateProfileDto.getFullName());
            account.setGender(updateProfileDto.getGender());
            account.setPhoneNumber(updateProfileDto.getPhoneNumber());
            account.setDob(updateProfileDto.getDob());
            accountRepository.save(account);

            Optional<?> getAccount = this.profileAccount();
            return Optional.of(getAccount);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> updateIsAbleSurvey(Integer accountId, Boolean isAbleSurvey) throws BadRequestException {
        Student student = studentRepository.findById(accountId).orElseThrow(() -> new UsernameNotFoundException("Not found student"));

        student.setIsEnableSurvey(isAbleSurvey);
        studentRepository.save(student);
        return Optional.of(student);
    }

    @Override
    public Account getCurrentAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new BadCredentialsException("Invalid authentication principal");
        }

        String email = ((UserDetails) principal).getUsername();

        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Account not found for email: " + email));
    }

    @Override
    public Optional<?> listAllCounselors() {
        try {
            List<Account> accounts = accountRepository.findCounselorsWithSlots();
            List<InfoCounselor> infoCounselors = accounts.stream()
                    .map(this::mapToDto).toList();
            return Optional.of(infoCounselors);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return Optional.of(e.getMessage());
        }
    }

    @Override
    public List<CounselorDto> getAllCounselors() {
        List<Counselor> counselors = counselorRepository.findAll();

        return counselors.stream()
                .map(counselorMapper::mapToCounselorDto)
                .toList();
    }


    private InfoCounselor mapToDto(Account account) {
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
