package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateProfileDto;
import com.fpt.gsu25se47.schoolpsychology.dto.response.*;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.repository.*;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final StudentRepository studentRepository;

    private final CounselorRepository counselorRepository;

    private final TeacherRepository teacherRepository;

    private final GuardianRepository guardianRepository;


    @Override
    public Optional<?> profileAccount() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if (account == null) {
                throw new UsernameNotFoundException("Not found user");
            }

            if (account.getRole().name().equalsIgnoreCase("STUDENT")) {
                Student student = studentRepository.findById(account.getId()).orElse(null);

                if (student == null) {
                    throw new UsernameNotFoundException("Not found student");
                }

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

                return Optional.of(studentDto);
            } else if (account.getRole().name().equalsIgnoreCase("PARENTS")) {
                Guardian guardian = guardianRepository.findById(account.getId()).orElse(null);

                if (guardian == null) {
                    throw new UsernameNotFoundException("Not found guardian");
                }

                ParentDto parentDto = new ParentDto();
                parentDto.setEmail(account.getEmail());
                parentDto.setDob(account.getDob());
                parentDto.setFullName(account.getFullName());
                parentDto.setGender(account.getGender());
                parentDto.setPhoneNumber(account.getPhoneNumber());
                parentDto.setAddress(guardian.getAddress());

                RelationshipDto relationshipDto = new RelationshipDto();
                List<Student> students = guardian.getRelationships().stream()
                        .map(Relationship::getStudent)
                        .filter(Objects::nonNull)
                        .toList();

                List<StudentDto> studentDtos = students.stream().map(item -> {
                    StudentDto studentDto = new StudentDto();
                    Account studentAcc = accountRepository.findById(item.getId()).orElse(null);
                    if (studentAcc == null) {
                        return null; // hoặc throw nếu bạn muốn chặt chẽ hơn
                    }

                    studentDto.setEmail(studentAcc.getEmail());
                    studentDto.setDob(studentAcc.getDob());
                    studentDto.setFullName(studentAcc.getFullName());
                    studentDto.setGender(studentAcc.getGender());
                    studentDto.setPhoneNumber(studentAcc.getPhoneNumber());
                    studentDto.setStudentCode(item.getStudentCode());
                    studentDto.setIsEnableSurvey(item.getIsEnableSurvey());

                    ClassDto classDto = new ClassDto();
                    if (item.getClasses() != null) {
                        classDto.setClassYear(item.getClasses().getClassYear());
                        classDto.setCodeClass(item.getClasses().getCodeClass());

                        if (item.getClasses().getTeacher() != null && item.getClasses().getTeacher().getAccount() != null) {
                            TeacherOfClassDto teacherDto = new TeacherOfClassDto();
                            teacherDto.setEmail(item.getClasses().getTeacher().getAccount().getEmail());
                            teacherDto.setFullName(item.getClasses().getTeacher().getAccount().getFullName());
                            teacherDto.setPhoneNumber(item.getClasses().getTeacher().getAccount().getPhoneNumber());
                            teacherDto.setTeacherCode(item.getClasses().getTeacher().getTeacherCode());
                            classDto.setTeacher(teacherDto);
                        }
                    }

                    studentDto.setClassDto(classDto);
                    return studentDto;
                }).filter(Objects::nonNull).toList();

                relationshipDto.setStudent(studentDtos);
                parentDto.setRelationships(relationshipDto);

                return Optional.of(parentDto);
            } else if (account.getRole().name().equalsIgnoreCase("COUNSELOR")){
               CounselorDto counselorDto = new CounselorDto();
               Counselor counselor = counselorRepository.findById(account.getId()).orElse(null);

               if (counselor == null) {
                   throw new UsernameNotFoundException("Not found counselor");
               }

               counselorDto.setCounselorCode(counselor.getCounselorCode());
               counselorDto.setPhoneNumber(account.getPhoneNumber());
               counselorDto.setEmail(account.getEmail());
               counselorDto.setFullName(account.getFullName());
               counselorDto.setGender(account.getGender());
               counselorDto.setLinkMeet(counselor.getLinkMeet());
               counselorDto.setGender(account.getGender());

               return Optional.of(counselorDto);
            } else {
                TeacherDto teacherDto = new TeacherDto();
                Teacher teacher = teacherRepository.findById(account.getId()).orElse(null);

                if (teacher == null) {
                    throw new UsernameNotFoundException("Not found teacher");
                }

                teacherDto.setEmail(account.getEmail());
                teacherDto.setFullName(account.getFullName());
                teacherDto.setPhoneNumber(account.getPhoneNumber());
                teacherDto.setGender(account.getGender());
                teacherDto.setFullName(account.getFullName());
                teacherDto.setTeacherCode(teacher.getTeacherCode());
                teacherDto.setLinkMeet(teacher.getLinkMeet());

                return Optional.of(teacherDto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }

    @Override
    public Optional<?> updateProfileAccount(UpdateProfileDto updateProfileDto) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account account = accountRepository.findByEmail(userDetails.getUsername()).orElse(null);

            if(account == null) {
                throw new UsernameNotFoundException("Not found account");
            }

            if(account.getRole().name().equalsIgnoreCase("STUDENT")){
                account.setFullName(updateProfileDto.getFullName());
                account.setGender(updateProfileDto.getGender());
                account.setPhoneNumber(updateProfileDto.getPhoneNumber());
                account.setDob(updateProfileDto.getDob());
                Student student = studentRepository.findById(account.getId()).orElse(null);

                if(student == null) {
                    throw new UsernameNotFoundException("Not found student");
                }

                student.setIsEnableSurvey(updateProfileDto.getIsEnableSurvey());
                studentRepository.save(student);
                accountRepository.save(account);


                Optional<?> getAccount = this.profileAccount();
                return Optional.of(getAccount);
            } else {
                account.setFullName(updateProfileDto.getFullName());
                account.setGender(updateProfileDto.getGender());
                account.setPhoneNumber(updateProfileDto.getPhoneNumber());
                account.setDob(updateProfileDto.getDob());
                accountRepository.save(account);

                Optional<?> getAccount = this.profileAccount();
                return Optional.of(getAccount);
            }


        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException("Something went wrong");
        }
    }


}
