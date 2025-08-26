package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateEnrollmentRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.EnrollmentResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.EnrollmentMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Account;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Enrollment;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.AccountRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.EnrollmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    private final EnrollmentMapper enrollmentMapper;
    private final AccountRepository accountRepository;

    @Override
    public List<EnrollmentResponse> createBulkEnrollment(CreateEnrollmentRequest request) {

        Classes clazz = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + request.getClassId()));

        if (!clazz.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "This class is inactive");
        }

        List<Student> students = studentRepository.findAllById(request.getStudentIds());

        if (students.size() != request.getStudentIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "One or more students not found");
        }

        validateConflictStudentIds(request);

        List<Enrollment> enrollmentsToSave = getEnrollmentsToSave(request, students, clazz);

        List<Enrollment> saved = enrollmentRepository.saveAll(enrollmentsToSave);

        return saved.stream()
                .map(enrollmentMapper::toEnrollmentResponse)
                .toList();
    }

//    @Override
//    public List<String> getConnectionUser(String email) {
//        Account account = accountRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        if(account.getRole() == Role.STUDENT) {
//            return account.getStudent().get
//        }
//    }

    private List<Enrollment> getEnrollmentsToSave(CreateEnrollmentRequest request, List<Student> students, Classes clazz) {
        // Filter out already enrolled students

        return students.stream()
                .map(student -> {
                    student.setTargetLevel(clazz.getGrade());
                    return enrollmentMapper.toEnrollment(request, student, clazz);
                })
                .toList();
    }

    private void validateConflictStudentIds(CreateEnrollmentRequest request) {
        List<Enrollment> existingEnrollments = enrollmentRepository.findAllByStudentIdIn(request.getStudentIds());

        List<Integer> conflictStudentIds = existingEnrollments.stream()
                .filter(e -> e.getClasses().getIsActive())
                .map(e -> e.getStudent().getId())
                .toList();

        if (!conflictStudentIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Some students are already enrolled in a different class: " + conflictStudentIds);
        }
    }
}
