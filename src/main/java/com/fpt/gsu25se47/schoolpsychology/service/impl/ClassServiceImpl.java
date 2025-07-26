package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ClassMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TeacherRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;

    private final ClassMapper classMapper;

    @Override
    public ClassResponse createClass(CreateClassRequest request) {

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Teacher not found for ID: " + request.getTeacherId()
                ));

        List<Classes> classesExisted = classRepository.findByCodeClass(request.getCodeClass());

        if (!classesExisted.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Class with code class: " + request.getCodeClass() + " already existed");
        }

        validateSchoolYear(request);

        Classes classes = classMapper.toClassEntity(request);
        classes.setTeacher(teacher);

        return classMapper.toClassResponse(classRepository.save(classes));
    }

    private static void validateSchoolYear(CreateClassRequest request) {

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