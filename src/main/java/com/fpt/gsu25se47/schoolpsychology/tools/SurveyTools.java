package com.fpt.gsu25se47.schoolpsychology.tools;

import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.SurveyGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.service.inter.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SurveyTools {

    private final SurveyService surveyService;

    @Tool(description = "For only role: MANAGER -> Get all available psychology surveys")
    public List<SurveyGetAllResponse> getAllSurveys() {
        return surveyService.getAllSurveys();
    }

    @Tool(description = "For all roles except TEACHER role -> Get detailed information for a specific survey by ID")
    public SurveyDetailResponse getSurveyById(Integer id, Boolean flag) {
        return surveyService.getSurveyById(id, flag);
    }

    @Tool(description = "For STUDENTS and PARENTS -> Get all published surveys available for a specific student. " +
            "If the current user role is STUDENT, take the current accountId and pass the to the studentId parameter")
    public List<SurveyGetAllResponse> getAllSurveyWithPublished(Integer studentId) {
        return surveyService.getAllSurveyWithPublished(studentId);
    }

    @Tool(description = "Get all surveys associated with a specific case")
    public List<SurveyGetAllResponse> getAllSurveyStudentInCase(Integer caseId) {
        return surveyService.getAllSurveyStudentInCase(caseId);
    }
}
