package com.fpt.gsu25se47.schoolpsychology.service.impl;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.request.UpdateClassRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Classes.ClassResponseSRC;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Student.StudentSRCResponse;
import com.fpt.gsu25se47.schoolpsychology.mapper.ClassMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.SchoolYearMapper;
import com.fpt.gsu25se47.schoolpsychology.mapper.TermMapper;
import com.fpt.gsu25se47.schoolpsychology.model.Classes;
import com.fpt.gsu25se47.schoolpsychology.model.ClassesTerm;
import com.fpt.gsu25se47.schoolpsychology.model.Teacher;
import com.fpt.gsu25se47.schoolpsychology.model.Term;
import com.fpt.gsu25se47.schoolpsychology.repository.ClassRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.SchoolYearRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.TeacherRepository;
import com.fpt.gsu25se47.schoolpsychology.service.inter.AccountService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.ClassService;
import com.fpt.gsu25se47.schoolpsychology.service.inter.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final SchoolYearRepository schoolYearRepository;

    private final AccountService accountService;
    private final TermService termService;

    private final SchoolYearMapper schoolYearMapper;
    private final TermMapper termMapper;
    private final ClassMapper classMapper;

    @Override
    public List<ClassResponse> createClass(List<CreateClassRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request list cannot be empty");
        }

        Teacher teacher = getTeacher(requests);

        // Validate each request
        validateCodeClasses(requests);

        List<Classes> classEntities = new ArrayList<>();

        List<Integer> missingSchoolYearIds = new ArrayList<>();

        // Map and assign teacher
        for (CreateClassRequest req : requests) {

            Integer schoolYearId = req.getSchoolYearId();
            boolean exists = schoolYearRepository.existsById(schoolYearId);
            if (!exists) {
                missingSchoolYearIds.add(schoolYearId);
            }

            List<Term> terms = termService.getTermsByYearId(req.getSchoolYearId());

            Classes cls = classMapper.toClassEntity(req);
            cls.setTeacher(teacher);

            List<ClassesTerm> classTerms = terms.stream()
                    .map(term -> {
                        ClassesTerm ct = new ClassesTerm();
                        ct.setClazz(cls);
                        ct.setTerm(term);
                        return ct;
                    })
                    .toList();

            cls.setClassesTerm(classTerms);

            classEntities.add(cls);
        }

        if (!missingSchoolYearIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "School year(s) not found: " + missingSchoolYearIds
            );
        }

        List<Classes> savedClasses = classRepository.saveAll(classEntities);

        return savedClasses.stream()
                .map(cl -> classMapper.toClassResponse(cl, termMapper, schoolYearMapper))
                .collect(Collectors.toList());
    }

    @Override
    public ClassResponse updateClass(Integer classId, UpdateClassRequest request) {

        Classes classes = classRepository.findById(classId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Class not found for ID: " + classId));
        classMapper.updateClassFromRequest(classes, request);

        if (request.getSchoolYearId() != null) {
            List<Term> terms = termService.getTermsByYearId(request.getSchoolYearId());

            // Existing list JPA is tracking
            List<ClassesTerm> existingClassTerms = classes.getClassesTerm();

            // Remove terms no longer present
            existingClassTerms.removeIf(ct -> terms.stream()
                    .noneMatch(t -> t.getId().equals(ct.getTerm().getId()))
            );

            // Add missing terms
            for (Term term : terms) {
                boolean alreadyExists = existingClassTerms.stream()
                        .anyMatch(ct -> ct.getTerm().getId().equals(term.getId()));
                if (!alreadyExists) {
                    ClassesTerm newCt = new ClassesTerm();
                    newCt.setClazz(classes);
                    newCt.setTerm(term);
                    existingClassTerms.add(newCt);
                }
            }
        }

        Classes saved = classRepository.save(classes);

        return classMapper.toClassResponse(saved, termMapper, schoolYearMapper);
    }

    @Override
    public ClassResponseSRC getClassByCode(String code) {

        Classes classes = classRepository.findByCodeClass(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + code));

        List<StudentSRCResponse> students = accountService.getStudentsByClassWithLSR(classes.getId());

        return classMapper.toClassDetailResponseSRC(classes, termMapper, schoolYearMapper, students);
    }

    @Override
    public ClassResponseSRC getClassById(Integer classId) {
        Classes classes = classRepository.findById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Class not found for ID: " + classId));

        List<StudentSRCResponse> students = accountService.getStudentsByClassWithLSR(classId);

        return classMapper.toClassDetailResponseSRC(classes, termMapper, schoolYearMapper, students);
    }

    @Override
    public List<ClassResponse> getAllClasses() {

        return classRepository.findAll()
                .stream()
                .map(cl -> classMapper.toClassResponse(cl, termMapper, schoolYearMapper))
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
}