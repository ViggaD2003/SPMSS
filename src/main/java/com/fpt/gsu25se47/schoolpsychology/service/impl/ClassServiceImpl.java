package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.ClassMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TeacherRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import com.fpt.gsu25se47.schoolpsychology.utils.DuplicateValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    //    private final ClassMapper classMapper;
    private final ClassMapper classMapper;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DuplicateValidationUtils duplicateValidationUtils;

    @Override
    public ClassDto createClass(ClassRequest request) {

        if (classRepository.findByCodeClass(request.getCodeClass()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Class with code '" + request.getCodeClass() + "' already exists."
            );
        }

        duplicateValidationUtils.validateStudentCodes(request.getStudentCodes());

        List<Student> students;

        if (request.getStudentCodes().isEmpty()) {
            students = Collections.emptyList();
        } else {
            students = request.getStudentCodes()
                    .stream()
                    .map(s -> studentRepository.findByStudentCode(s)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Student not found with StudentCode: " + s)))
                    .toList();
        }

        Teacher teacher = teacherRepository.findByTeacherCode(request.getTeacherCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found with code: " + request.getTeacherCode()
                ));

        Classes classes = classMapper.mapToClass(request,
                students,
                teacher);

        students.forEach(t -> t.setClasses(classes));

        return classMapper.mapToClassDto(
                classRepository.save(classes)
        );
    }

    @Override
    public ClassDto updateClass(String code, ClassRequest request) {

        Classes classes = classRepository.findByCodeClass(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Class not found with ID: " + code
                ));

        List<Student> students;

        if (request.getStudentCodes().isEmpty()) {
            students = Collections.emptyList();
        } else {
            students = request.getStudentCodes()
                    .stream()
                    .map(s -> studentRepository.findByStudentCode(s)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Student not found with StudentCode: " + s)))
                    .toList();
        }

        Teacher teacher = teacherRepository.findByTeacherCode(request.getTeacherCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found with code: " + request.getTeacherCode()
                ));

        Classes classesMapped = classMapper.mapToClass(request, students, teacher);

        classesMapped.setId(classes.getId());

        return classMapper.mapToClassDto(
                classRepository.save(classesMapped));
    }

    @Override
    public ClassDto getClassByCode(String code) {

        Classes classes = classRepository.findByCodeClass(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Class not found with Code: " + code
                ));

        return classMapper.mapToClassDto(
                classRepository.save(classes));
    }

    @Override
    public List<ClassDto> getAllClasses() {
        List<Classes> classes = classRepository.findAll();

        return classes.stream()
                .map(classMapper::mapToClassDto)
                .toList();
    }

    @Override
    public void deleteClassByCode(String code) {
        Classes classes = classRepository.findByCodeClass(code)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Class not found with code: " + code
                ));

        List<Student> students = classes.getStudents();
        if (!students.isEmpty()) {
            students.forEach(
                    t -> t.setClasses(null)
            );
            studentRepository.saveAll(students);
        }

        classRepository.delete(classes);
    }
}
