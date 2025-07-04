package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.ClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassDto;
import com.fpt.gsu25se47.schoolpsychology.mapper.ClassMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassMapper classMapper;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;

    @Override
    public ClassDto createClass(ClassRequest request) {

        Optional<Classes> existingClassOpt = classRepository.findByCodeClass(request.getCodeClass());
        if (existingClassOpt.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Class with code '" + request.getCodeClass() + "' already exists."
            );
        }

        Classes classes = classMapper.mapToClass(request);

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
        Classes classesMapped = classMapper.mapToClass(request);

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
