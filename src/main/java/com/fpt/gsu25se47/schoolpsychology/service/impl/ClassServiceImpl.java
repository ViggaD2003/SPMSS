package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.StudentDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.ClassMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.StudentMapper;
import com.fpt.gsu25se47.schoolpsychology.model.*;
import com.fpt.gsu25se47.schoolpsychology.model.enums.Role;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.EnrollmentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TeacherRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final AccountService accountService;

    private final StudentMapper studentMapper;
    private final ClassMapper classMapper;

    @Override
    public List<ClassResponse> createClass(List<CreateClassRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list cannot be empty");
        }

        // Validate all teacher IDs are the same (or you can adapt if mixed teachers allowed)
        Teacher teacher = getTeacher(requests);

        // Validate each request
        validateCodeClasses(requests);

        requests.forEach(this::validateSchoolYear);

        // Map and assign teacher
        List<Classes> classEntities = requests.stream()
                .map(req -> {
                    Classes cls = classMapper.toClassEntity(req);
                    cls.setTeacher(teacher);
                    return cls;
                }).collect(Collectors.toList());

        List<Classes> savedClasses = classRepository.saveAll(classEntities);

        return savedClasses.stream()
                .map(classMapper::toClassResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassResponse getClassByCode(String code) {

        Classes classes = classRepository.findByCodeClass(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + code));

        List<Student> studentsInClass = enrollmentRepository.findStudentsByClassesId(classes.getId());
        List<StudentDto> studentDtos = studentsInClass.stream()
                .map(studentMapper::mapStudentDtoWithoutClass)
                .toList();

        return classMapper.toClassDetailResponse(classes, studentDtos);
    }

    @Override
    public ClassResponse getClassById(Integer classId) {
        Classes classes = classRepository.findById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + classId));

        List<Student> studentsInClass = enrollmentRepository.findStudentsByClassesId(classId);
        List<StudentDto> studentDtos = studentsInClass.stream()
                .map(studentMapper::mapStudentDtoWithoutClass)
                .toList();

        return classMapper.toClassDetailResponse(classes, studentDtos);
    }

    @Override
    public List<ClassResponse> getAllClasses() {

        Account account = accountService.getCurrentAccount();

        if (account.getRole() == Role.TEACHER) {
            return getClassResponsesForTeacher(account);
        }

        return classRepository.findAll()
                .stream()
                .map(classMapper::toClassResponse)
                .toList();
    }

    private List<ClassResponse> getClassResponsesForTeacher(Account account) {

        List<Classes> classes = classRepository.findAllByTeacherId(account.getId());

        List<StudentDto> studentDtos = classes.stream()
                .flatMap(c -> c.getEnrollments()
                        .stream()
                        .map(Enrollment::getStudent)
                        .map(studentMapper::mapStudentDtoWithoutClass))
                .toList();

        return classes.stream()
                .map(s -> classMapper.toClassDetailResponse(s, studentDtos))
                .toList();
    }

    private Teacher getTeacher(List<CreateClassRequest> requests) {

        Integer teacherId = requests.getFirst().getTeacherId();
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found for ID: " + teacherId
                ));
    }

    private void validateCodeClasses(List<CreateClassRequest> requests) {

        List<String> codeClasses = requests.stream()
                .map(CreateClassRequest::getCodeClass)
                .collect(Collectors.toList());

        List<Classes> existingClasses = classRepository.findByCodeClassIn(codeClasses);
        if (!existingClasses.isEmpty()) {
            String existingCodes = existingClasses.stream()
                    .map(Classes::getCodeClass)
                    .collect(Collectors.joining(", "));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Classes with code(s): " + existingCodes + " already exist");
        }
    }

    private void validateSchoolYear(CreateClassRequest request) {

        String[] schoolYear = request.getSchoolYear().split("-");
        int firstYear = Integer.parseInt(schoolYear[0]);
        int lastYear = Integer.parseInt(schoolYear[1]);
        int duration = lastYear - firstYear;

        int startYear = request.getStartTime().getYear();
        int endYear = request.getEndTime().getYear();
        int durationTime = endYear - startYear;
        if (durationTime != duration) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Start time and End time not valid");
        }
    }
}