package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.request.CreateMentalEvaluationRequest;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationResponse;
import com.fpt.gsu25se47.schoolpsychology.model.Category;
import com.fpt.gsu25se47.schoolpsychology.model.MentalEvaluation;
import com.fpt.gsu25se47.schoolpsychology.model.Student;
import com.fpt.gsu25se47.schoolpsychology.repository.CategoryRepository;
import com.fpt.gsu25se47.schoolpsychology.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class MentalEvaluationMapper {

    private final CategoryRepository categoryRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CategoryMapper categoryMapper;

    public MentalEvaluation mapToMentalEvaluation(CreateMentalEvaluationRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found"));

        return MentalEvaluation.builder()
                .evaluationRecordId(request.getEvaluationRecordId())
                .date(request.getDate())
                .totalScore(request.getTotalScore())
                .evaluationType(request.getEvaluationType())
                .category(category)
                .student(student)
                .build();
    }

    public MentalEvaluationResponse mapToEvaluationResponse(MentalEvaluation mentalEvaluation) {

        return MentalEvaluationResponse.builder()
                .id(mentalEvaluation.getId())
                .evaluationType(mentalEvaluation.getEvaluationType().name())
                .evaluationRecordId(mentalEvaluation.getEvaluationRecordId())
                .date(mentalEvaluation.getDate())
                .totalScore(mentalEvaluation.getTotalScore())
                .student(studentMapper.mapToStudentDto(mentalEvaluation.getStudent()))
                .category(categoryMapper.toResponse(mentalEvaluation.getCategory()))
                .build();
    }
}
