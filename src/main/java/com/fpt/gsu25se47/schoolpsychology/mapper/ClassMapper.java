package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ClassMapper {

    private final TeacherOfClassMapper teacherOfClassMapper;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public Classes mapToClass(ClassRequest request) {

        validateStudentCodes(request.getStudentCodes());

        List<Student> students;

        if (request.getStudentCodes().isEmpty()) {
            students = Collections.emptyList();
        } else {
            students = request.getStudentCodes()
                    .stream()
                    .map(studentRepository::findByStudentCode)
                    .toList();
        }

        Teacher teacher = teacherRepository.findByTeacherCode(request.getTeacherCode())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found with code: " + request.getTeacherCode()
                ));

        Classes classes = Classes.builder()
                .codeClass(request.getCodeClass())
                .classYear(request.getClassYear())
                .teacher(teacher)
                .build();

        students.forEach(t -> t.setClasses(classes));

        classes.setStudents(students);

        return classes;
    }

    public ClassDto mapToClassDto(Classes classes) {

        return ClassDto.builder()
                .codeClass(classes.getCodeClass())
                .classYear(classes.getClassYear())
                .teacher(classes.getTeacher() != null
                        ? teacherOfClassMapper.mapToTeacherOfClassDto(classes.getTeacher())
                        : null)
                .build();
    }

    private void validateStudentCodes(List<String> studentCodes) {
        Set<String> seenStudentCodes = new HashSet<>();

        for (String sd : studentCodes) {
            if (!seenStudentCodes.add(sd)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Duplicate studentCodes found: " + sd
                );
            }
        }
    }
}
