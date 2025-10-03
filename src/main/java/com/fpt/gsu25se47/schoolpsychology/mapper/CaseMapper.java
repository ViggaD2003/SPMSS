package com.fpt.gsu25se47.schoolpsychology.mapper;

import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllForStudentResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetAllResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.Cases.CaseGetDetailResponse;
import com.fpt.gsu25se47.schoolpsychology.dto.response.MentalEvaluationStatic;
import com.fpt.gsu25se47.schoolpsychology.model.Cases;
import com.fpt.gsu25se47.schoolpsychology.repository.SurveyRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseMapper {

    private final LevelMapper levelMapper;
    private final AccountMapper accountMapper;
    private final SurveyRecordRepository surveyRecordRepository;

    public CaseGetAllResponse mapToCaseGetAllResponse(Cases cases, Integer surveyId) {
        Integer alreadyDoneSurveyRecord = surveyRecordRepository.isSurveyRecordCaseByCaseId(cases.getId(), surveyId);

        return CaseGetAllResponse.builder()
                .id(cases.getId())
                .categoryId(cases.getInitialLevel().getCategory().getId())
                .categoryName(cases.getInitialLevel().getCategory().getName())
                .codeCategory(cases.getInitialLevel().getCategory().getCode())
                .title(cases.getTitle())
                .description(cases.getDescription())
                .priority(cases.getPriority())
                .status(cases.getStatus())
                .progressTrend(cases.getProgressTrend())
                .createBy(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCreateBy()))
                .isAddSurvey(surveyId == null ? null : cases.getSurveyCaseLinks().stream().anyMatch(item -> item.getSurvey().getId() == surveyId  && item.getIsActive()))
                .counselor(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCounselor()))
                .student(cases.getStudent() == null ? null : accountMapper.toDto(cases.getStudent()))
                .initialLevel(levelMapper.mapToLevelResponse(cases.getInitialLevel()))
                .currentLevel(levelMapper.mapToLevelResponse(cases.getCurrentLevel()))
                .notify(cases.getNotify())
                .createdAt(cases.getCreatedDate())
                .updatedAt(cases.getUpdatedDate())
                .alreadyDoneSurvey(alreadyDoneSurveyRecord)
                .build();
    }

    public CaseGetAllForStudentResponse mapToCaseGetAllForStudentResponse(Cases cases, Integer surveyId) {
        return CaseGetAllForStudentResponse.builder()
                .id(cases.getId())
                .categoryId(cases.getInitialLevel().getCategory().getId())
                .categoryName(cases.getInitialLevel().getCategory().getName())
                .codeCategory(cases.getInitialLevel().getCategory().getCode())
                .title(cases.getTitle())
                .description(cases.getDescription())
                .priority(cases.getPriority())
                .status(cases.getStatus())
                .progressTrend(cases.getProgressTrend())
                .createBy(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCreateBy()))
                .isAddSurvey(surveyId == null ? null : cases.getSurveyCaseLinks().stream().anyMatch(item -> item.getSurvey().getId() == surveyId))
                .counselor(cases.getCreateBy() == null ? null : accountMapper.toDto(cases.getCounselor()))
                .initialLevel(levelMapper.mapToLevelResponse(cases.getInitialLevel()))
                .currentLevel(levelMapper.mapToLevelResponse(cases.getCurrentLevel()))
                .createdAt(cases.getCreatedDate())
                .updatedAt(cases.getUpdatedDate())
                .build();
    }

    public CaseGetDetailResponse mapCaseGetDetailResponse(Cases cases, MentalEvaluationStatic statics) {
        return CaseGetDetailResponse.builder()
                .caseInfo(mapToCaseGetAllResponse(cases, null))
                .groupedStatic(statics)
                .build();
    }
}
